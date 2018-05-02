package crypto.client.model;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Config {

	public static String DEFAULT_SYMBOL = "USD";

	public static int LIVE_FEED_RATE = 10;

	private static String FILE_PATH = "files/settings.ser";

	public static void saveToDisk() {
		try {
			FileOutputStream fos = new FileOutputStream(FILE_PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeUTF(DEFAULT_SYMBOL);
			oos.writeInt(LIVE_FEED_RATE);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadFromDisk() {
		try {
			FileInputStream fis = new FileInputStream(FILE_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			DEFAULT_SYMBOL = ois.readUTF();
			LIVE_FEED_RATE = ois.readInt();
			ois.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
