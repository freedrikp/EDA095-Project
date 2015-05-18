package WeezelTV.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Configuration {
	
	// General
	public static int COM_PORT = 7374;
	
	// Client
	public static int CLIENT_BUFFER_SIZE = 100;
	public static String CLIENT_HOST = "localhost";
	
	// Server
	public static float SERVER_COMPRESSION_QUALITY = 0.3f;
	public static String SERVER_MEDIA_DIRECTORY = "media";
	public static int SERVER_BUFFER_SIZE = 100;
	public static long SERVER_WAIT_TIME = 0;
	public static int SERVER_BLOCK_SIZE = 0;

	public static void loadConfiguration(String file){
		try {
			Scanner scan = new Scanner(new File(file));
			while(scan.hasNextLine()){
				String line = scan.nextLine().trim();
				if (!(line.isEmpty() || line.startsWith("#"))){
					String[] par = line.split("=");
					switch (par[0].trim()){
					case "COM_PORT":
						COM_PORT = Integer.parseInt(par[1].trim());
						break;					
					case "CLIENT_BUFFER_SIZE":
						CLIENT_BUFFER_SIZE = Integer.parseInt(par[1].trim());
						break;
					case "CLIENT_HOST":
						CLIENT_HOST = par[1].trim();
						break;
					case "SERVER_COMPRESSION_QUALITY":
						SERVER_COMPRESSION_QUALITY = Float.parseFloat(par[1].trim());
						break;
					case "SERVER_MEDIA_DIRECTORY":
						SERVER_MEDIA_DIRECTORY = par[1].trim();
						break;
					case "SERVER_BUFFER_SIZE":
						SERVER_BUFFER_SIZE = Integer.parseInt(par[1].trim());
						break;
					case "SERVER_WAIT_TIME":
						SERVER_WAIT_TIME = Integer.parseInt(par[1].trim());
						break;
					case "SERVER_BLOCK_SIZE":
						SERVER_BLOCK_SIZE = Integer.parseInt(par[1].trim());
						break;
					default:
						System.out.println("Unknown parameter in " + file  + ": " + line.trim());
						continue;
					}
					System.out.println("Parameter " + par[0].trim() + " loaded with " + par[1].trim());
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
