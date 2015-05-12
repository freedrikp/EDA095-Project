package SimpleExample.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import SimpleExample.common.Configuration;

@SuppressWarnings("rawtypes")
public class ClientGui {
	private ClientSender cSender;
	private ClientBuffer cBuffer;
	private Socket socket;
	private JFrame frame;
	private JLabel movieScreen;
	private JLabel nbrOfFrames;
	private JLabel nbrOfAudioSamples;
	private boolean fullscreen = false;
	private boolean firstImage = true;
	private boolean firstTimeBufferLoaded = true;
	private boolean firstSelect = true;
	private JProgressBar progressBar;
	private JButton btnPlay;
	private JButton btnStreamPlay;
	private JList movieList;

	public ClientGui(ClientSender cSender, ClientBuffer cBuffer) {
		this.cSender = cSender;
		this.cBuffer = cBuffer;
		initialize();
	}

	@SuppressWarnings({ "unchecked" })
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 864, 486);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Main Panel", null, mainPanel, null);
		tabbedPane.setEnabledAt(0, true);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JPanel movieScreenPanel = new JPanel();
		movieScreenPanel.setBackground(Color.DARK_GRAY);
		movieScreenPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		mainPanel.add(movieScreenPanel, BorderLayout.CENTER);

		movieScreen = new JLabel();
		movieScreenPanel.add(movieScreen);
		movieScreen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!fullscreen) {
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					} else {
						frame.setExtendedState(JFrame.NORMAL);

					}
					fullscreen = !fullscreen;
				}
				if (cBuffer.isPlaying()) {
					cBuffer.setPlayNotPause(false);
					btnPlay.setText("Play");
				} else {
					cBuffer.setPlayNotPause(true);
					btnPlay.setText("Pause");
				}
			}
		});
		movieScreen.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout(0, 0));
		buttonPanel.setBackground(Color.GRAY);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		JPanel actionPanel = new JPanel();
		actionPanel.setBackground(Color.GRAY);
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.add(actionPanel);

		JPanel statsPanel = new JPanel();
		statsPanel.setBackground(Color.GRAY);
		statsPanel.setLayout(new BorderLayout(0, 0));
		actionPanel.add(statsPanel);

		nbrOfFrames = new JLabel();
		nbrOfFrames.setForeground(Color.WHITE);
		statsPanel.add(nbrOfFrames, BorderLayout.NORTH);

		nbrOfAudioSamples = new JLabel();
		nbrOfAudioSamples.setForeground(Color.WHITE);
		statsPanel.add(nbrOfAudioSamples, BorderLayout.SOUTH);

		progressBar = new JProgressBar(0, Configuration.CLIENT_BUFFER_SIZE);
		progressBar.setForeground(Color.LIGHT_GRAY);
		progressBar.setBackground(Color.DARK_GRAY);
		progressBar.setValue(0);
		actionPanel.add(progressBar);

		btnPlay = new JButton("Play");
		btnPlay.setEnabled(false);
		actionPanel.add(btnPlay);

		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cBuffer.isPlaying()) {
					btnPlay.setText("Play");
				} else {
					btnPlay.setText("Pause");
				}
				cBuffer.setPlayNotPause(!cBuffer.isPlaying());
			}
		});

		JPanel exitPanel = new JPanel();
		exitPanel.setBackground(Color.GRAY);
		buttonPanel.add(exitPanel, BorderLayout.EAST);

		btnStreamPlay = new JButton("Start stream");
		btnStreamPlay.setEnabled(false);
		btnStreamPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (btnStreamPlay.getText().equals("Start stream")) {
						cSender.sendPlayStream();
						btnStreamPlay.setText("Pause stream");
					} else {
						cSender.sendPauseStream();
						btnStreamPlay.setText("Start stream");
					}
				} catch (IOException e1) {
					btnStreamPlay.setEnabled(false);
				}
			}
		});
		exitPanel.add(btnStreamPlay);

		JButton btnExit = new JButton("Exit");
		exitPanel.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cSender.sendClose();
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		final JPanel selectPanel = new JPanel();
		selectPanel.setBackground(Color.DARK_GRAY);
		tabbedPane.addTab("Select Panel", null, selectPanel, null);
		selectPanel.setLayout(new BorderLayout(0, 0));

		movieList = new JList(cBuffer.getMovieList());
		movieList.setBorder(UIManager
				.getBorder("List.focusCellHighlightBorder"));
		movieList.setBackground(Color.DARK_GRAY);
		movieList.setForeground(Color.WHITE);
		selectPanel.add(movieList);

		JPanel selectButtonPanel = new JPanel();
		selectButtonPanel.setBackground(Color.GRAY);
		selectPanel.add(selectButtonPanel, BorderLayout.SOUTH);

		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!firstSelect) {
						startNewMovie();
					} else {
						firstSelect = false;
					}
					cSender.sendTitle(movieList.getSelectedValue().toString());
					btnStreamPlay.setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		selectButtonPanel.add(btnSelect);
		tabbedPane.setEnabledAt(1, true);

		frame.setVisible(true);
	}

	public void setImage(BufferedImage image) {
		if (firstImage) {
			frame.setBounds(100, 100, image.getWidth(), image.getHeight() + 125);
			frame.setLocationRelativeTo(null);
			firstImage = false;
		}
		if (fullscreen) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screenSize.getWidth();
			int height = (int) (screenSize.getHeight() / 1.3);
			movieScreen.setSize(width, height);
			BufferedImage fullscreenImage = new BufferedImage(
					movieScreen.getWidth(), movieScreen.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = fullscreenImage.createGraphics();
			g.drawImage(image, 0, 0, movieScreen.getWidth(),
					movieScreen.getHeight(), null);
			g.dispose();
			image = fullscreenImage;
		}
		movieScreen.setIcon(new ImageIcon(image));

	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void updateProgressBar() {
		int bufferSize = cBuffer.getSize();
		if (bufferSize <= Configuration.CLIENT_BUFFER_SIZE) {
			progressBar.setValue(bufferSize);
		} else if (firstTimeBufferLoaded) {
			cBuffer.setPlayNotPause(true);
			btnPlay.setText("Pause");
			btnPlay.setEnabled(true);
			firstTimeBufferLoaded = false;
			progressBar.setValue(Configuration.CLIENT_BUFFER_SIZE);
		}
		nbrOfFrames.setText(bufferSize + " frames\n");
		nbrOfAudioSamples.setText(cBuffer.getAudioSize() + " audio samples");
	}

	private void startNewMovie() throws UnknownHostException, IOException {
		cSender.sendClose();
		socket.close();
		socket = new Socket(Configuration.CLIENT_HOST, Configuration.COM_PORT);
		cSender.setSocket(socket);
		cBuffer.reset();
		new ClientReceiver(cBuffer, socket).start();
		firstImage = true;
		firstTimeBufferLoaded = true;
		btnStreamPlay.setText("Start stream");
		btnPlay.setText("Play");
		btnPlay.setEnabled(false);
		new ClientImageViewer(cBuffer, this).start();
		new ClientSoundPlayer(cBuffer).start();
	}
}
