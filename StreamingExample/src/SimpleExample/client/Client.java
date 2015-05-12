package SimpleExample.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import SimpleExample.common.Configuration;

public class Client {
	public static void main(String[] args) {
		try {
			Configuration.loadConfiguration("config.txt");
			Socket socket = new Socket(Configuration.CLIENT_HOST,
					Configuration.COM_PORT);
			ClientSender cs = new ClientSender(socket);
			ClientBuffer cib = new ClientBuffer();
			cs.sendGetMovieList();
			ClientReceiver ir = new ClientReceiver(cib, socket);
			ir.start();
			ClientGui gui = new ClientGui(cs, cib);
			ClientGuiUpdater ugui = new ClientGuiUpdater(gui);
			ugui.start();
			ClientSoundPlayer soundPlayer = new ClientSoundPlayer(cib);
			soundPlayer.start();
			gui.setSocket(socket);
			ClientImageViewer civ = new ClientImageViewer(cib, gui);
			civ.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
