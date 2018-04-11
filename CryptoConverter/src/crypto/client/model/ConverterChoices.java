package crypto.client.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import crypto.util.ConverterData;
import crypto.util.Pair;

public class ConverterChoices{
	
	ArrayList<ConverterData> dataList;
	
	public ConverterChoices() {
		dataList = new ArrayList<ConverterData>();
	}
	
	public void addData(ConverterData data) {
		dataList.add(data);
	}
	
	public void saveToFile(String file) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(dataList);

	}
	
	public void loadFromFile(String file) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		dataList = (ArrayList<ConverterData>)ois.readObject();
	}
	
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
