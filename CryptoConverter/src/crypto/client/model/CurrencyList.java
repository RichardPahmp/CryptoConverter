package crypto.client.model;

import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class CurrencyList {


    private static CoinList coinList;
    private static ArrayList<Currency> currencyList;
    private static String filename = "files/currencyList.txt";


    /**
     * Returns a CoinList containing all available Coins from the CryptoCompareAPI.
     *
     * @return CoinList A list of CoinEntry objects.
     */
    public static CoinList getCoinList() {
        if (coinList == null) {
            try {
                coinList = Coins.getCoinList();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfCallsException e) {
                e.printStackTrace();
            }
        }
        return coinList;
    }


    public static Currency getCurrency(String symbol) {
        Currency currency = null;
        for (Currency check : currencyList) {
            if (check.getSymbol().equals(symbol)) {
               currency = check;
            }
        }
        return currency;
    }


    /**
     * Get a list of all available CryptoCurrencies in a list of Currency objects.
     *
     * @return ArrayList<Currency> An ArrayList of currency objects.
     */
    public static ArrayList<Currency> getCurrencyList() {
        if (coinList == null) {
            getCoinList();
        }
        if (currencyList == null) {
            currencyList = new ArrayList<Currency>();
        }
        if (currencyList.size() <= 0) {
            for (CoinEntry coin : coinList.coins.values()) {
                currencyList.add(new Currency(coin));
            }
            currencyList.sort(Comparator.comparing(Currency::getCoinFullName));

        }
        return currencyList;
    }

    /**
     * Reads a list of fiat-currencies from a textfile, and loads them to the top of the currencylist.
     *
     * @throws IOException
     */

    public static void loadCurrencyList() throws IOException {
        currencyList = getCurrencyList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split(",");

                String symbol = itemPieces[0];
                String coinName = itemPieces[1];
                String coinNameFull = itemPieces[2];

                Currency currency = new Currency(symbol, coinName, coinNameFull);
                currencyList.add(0, currency);
            }
        } finally {
            if (br != null)
                br.close();
        }
    }
}
