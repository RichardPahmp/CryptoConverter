package crypto.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A utility class for saving and loading a list of ConverterData from a file.
 * @author Richard
 *
 */
public class ConverterChoices{
	
	ArrayList<ConverterData> dataList;
	
	public ConverterChoices() {
		dataList = new ArrayList<ConverterData>();
	}
	
	/**
	 * Adds the given data to the list.
	 * @param data The ConverterData to add.
	 */
	public void addData(ConverterData data) {
		dataList.add(data);
	}
	
	/**
	 * Writes the list of ConverterData to the file at the given URL.
	 * @param file The filepath to save to.
	 * @throws IOException
	 */
	public void saveToFile(String file) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(dataList);

	}
	
	/**
	 * Loads the list of ConverterData from the given filepath.
	 * @param file The filepath to load from.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadFromFile(String file) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		dataList = (ArrayList<ConverterData>)ois.readObject();
	}
	
	/**
	 * Get an iterator for the list of ConverterData.
	 * @return 
	 */
	public Iterator<ConverterData> getIterator(){
		return dataList.iterator();
	}
	
	public String toString() {
		String res = "";
		for(ConverterData data : dataList) {
			res += data.toString() + "\n";
		}
		return res;
	}
}
