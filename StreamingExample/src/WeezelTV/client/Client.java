package WeezelTV.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import WeezelTV.common.Configuration;

public class Client {
	public static void main(String[] args) {
		try {
			Configuration.loadConfiguration("config.txt");
			Socket socket = new Socket(Configuration.CLIENT_HOST,
					Configuration.COM_PORT);
			ClientSender cSender = new ClientSender(socket);
			ClientBuffer cBuffer = new ClientBuffer();
			cSender.sendGetMovieList();
			ClientReceiver cReceiver = new ClientReceiver(cBuffer, socket);
			cReceiver.start();
			ClientGui gui = new ClientGui(cSender, cBuffer);
			ClientGuiUpdater guiUpdater = new ClientGuiUpdater(gui);
			guiUpdater.start();
			ClientSoundPlayer soundPlayer = new ClientSoundPlayer(cBuffer);
			soundPlayer.start();
			gui.setSocket(socket);
			ClientImageViewer cImageViewer = new ClientImageViewer(cBuffer, gui);
			cImageViewer.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
