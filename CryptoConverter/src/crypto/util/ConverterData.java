package crypto.util;

import java.io.Serializable;
import java.time.LocalDate;

public class ConverterData implements Serializable{
	
	private String leftCurrency, rightCurrency;
	private LocalDate date;
	private double leftSum, rightSum;
	
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
