
			/*
			 * Now we have a packet, let's see if it belongs to our video stream
			 */
			if (packet.getStreamIndex() == videoStreamId) {
				/*
				 * We allocate a new picture to get the data out of Xuggler
				 */
				IVideoPicture picture = IVideoPicture.make(
						videoCoder.getPixelType(), videoCoder.getWidth(),
						videoCoder.getHeight());

				int offset = 0;
				while (offset < packet.getSize()) {
					/*
					 * Now, we decode the video, checking for any errors.
					 */
					int bytesDecoded = videoCoder.decodeVideo(picture, packet,
							offset);
					if (bytesDecoded < 0)
						throw new RuntimeException(
								"got error decoding video in: " + filename);
					offset += bytesDecoded;

					/*
					 * Some decoders will consume data in a packet, but will not
					 * be able to construct a full video picture yet. Therefore
					 * you should always check if you got a complete picture
					 * from the decoder
					 */
					if (picture.isComplete()) {
						IVideoPicture newPic = picture;
						/*
						 * If the resampler is not null, that means we didn't
						 * get the video in BGR24 format and need to convert it
						 * into BGR24 format.
						 */
						if (resampler != null) {
							// we must resample
							newPic = IVideoPicture.make(
									resampler.getOutputPixelFormat(),
									picture.getWidth(), picture.getHeight());
							if (resampler.resample(newPic, picture) < 0)
								throw new RuntimeException(
										"could not resample video from: "
												+ filename);
						}
						if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
							throw new RuntimeException("could not decode video"
									+ " as BGR 24 bit data in: " + filename);

						/**
						 * We could just display the images as quickly as we
						 * decode them, but it turns out we can decode a lot
						 * faster than you think.
						 * 
						 * So instead, the following code does a poor-man's
						 * version of trying to match up the frame-rate
						 * requested for each IVideoPicture with the system
						 * clock time on your computer.
						 * 
						 * Remember that all Xuggler IAudioSamples and
						 * IVideoPicture objects always give timestamps in
						 * Microseconds, relative to the first decoded item. If
						 * instead you used the packet timestamps, they can be
						 * in different units depending on your IContainer, and
						 * IStream and things can get hairy quickly.
						 */
						if (firstTimestampInStream == Global.NO_PTS) {
							// This is our first time through
							firstTimestampInStream = picture.getTimeStamp();
							// get the starting clock time so we can hold up
							// frames
							// until the right time.
							systemClockStartTime = System.currentTimeMillis();
						} else {
							long systemClockCurrentTime = System
									.currentTimeMillis();
							long millisecondsClockTimeSinceStartofVideo = systemClockCurrentTime
									- systemClockStartTime;
							// compute how long for this frame since the first
							// frame in the
							// stream.
							// remember that IVideoPicture and IAudioSamples
							// timestamps are
							// always in MICROSECONDS,
							// so we divide by 1000 to get milliseconds.
							long millisecondsStreamTimeSinceStartOfVideo = (picture
									.getTimeStamp() - firstTimestampInStream) / 1000;
							final long millisecondsTolerance = 50; // and we
																	// give
																	// ourselfs
																	// 50 ms of
																	// tolerance
							final long millisecondsToSleep = (millisecondsStreamTimeSinceStartOfVideo - (millisecondsClockTimeSinceStartofVideo + millisecondsTolerance));
							if (millisecondsToSleep > 0) {
								try {
									Thread.sleep(millisecondsToSleep);
								} catch (InterruptedException e) {
									// we might get this when the user closes
									// the dialog box, so
									// just return from the method.
									return;
								}
							}
						}

						// And finally, convert the BGR24 to an Java buffered
						// image
						BufferedImage javaImage = Utils
								.videoPictureToImage(newPic);

						// and display it on the Java Swing window
						updateJavaWindow(javaImage);
					}
				}
			} else {
				/*
				 * This packet isn't part of our video stream, so we just
				 * silently drop it.
				 */
				do {
				} while (false);
			}

		
