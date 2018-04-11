package crypto.util;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Used for saving and restoring the data in a ConverterPane.
 * @author Richard
 *
 */
public class ConverterData implements Serializable{
	
	private String leftCurrency, rightCurrency;
	private LocalDate date;
	private double leftSum, rightSum;
	
	/**
	 * Initialize a ConverterData with the given arguments.
	 * @param leftCurrency The symbol of the currency entered in the left comboBox
	 * @param rightCurrency The symbol of the currency entered in the right comboBox
	 * @param date The chosen date for conversion. Can be left null to use the current date.
	 * @param leftSum The sum entered in the left textfield
	 * @param rightSum The sum entered in the right textfield
	 */
	public ConverterData(String leftCurrency, String rightCurrency, LocalDate date, double leftSum, double rightSum) {
		this.leftCurrency = leftCurrency;
		this.rightCurrency = rightCurrency;
		this.date = date;
		this.leftSum = leftSum;
		this.rightSum = rightSum;
	}

	public String getLeftCurrency() {
		return leftCurrency;
	}

	public String getRightCurrency() {
		return rightCurrency;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getLeftSum() {
		return leftSum;
	}

	public double getRightSum() {
		return rightSum;
	}
}
