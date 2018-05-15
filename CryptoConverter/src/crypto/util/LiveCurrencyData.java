package crypto.util;

import crypto.client.model.Currency;
import crypto.client.model.LiveCurrency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A utility class for saving and loading a list of LiveCurrency-objects from a file.
 * @author Antoine
 *
 */
public class LiveCurrencyData{

    ArrayList<Currency> dataList;

    public LiveCurrencyData() {
        dataList = new ArrayList<Currency>();
    }

    /**
     * Adds the given data to the list.
     * @param currency The LiveCurrencies to add.
     */
    public void addData(Currency currency) {
        dataList.add(currency);
    }

    /**
     * Writes the list of LiveCurrencies to the file at the given URL.
     * @param file The filepath to save to.
     * @throws IOException
     */
    public void saveToFile(String file) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(dataList);

    }

    /**
     * Loads the list of LiveCurrencies from the given filepath.
     * @param file The filepath to load from.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadFromFile(String file) throws FileNotFoundException, IOException, ClassNotFoundException, EOFException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        dataList = (ArrayList<Currency>)ois.readObject();
    }

    /**
     * Get an iterator for the list of LiveCurrency.
     */

    public Iterator<Currency> getIterator(){
        return dataList.iterator();
    }

    public String toString() {
        String res = "";
        for(Currency currency : dataList) {
            res += currency.toString() + "\n";
        }
        return res;
    }
}
