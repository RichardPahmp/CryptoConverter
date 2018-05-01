package crypto.client;

import java.time.LocalDate;

import crypto.client.model.Currency;

/**
 * An interface for callbacks from a ConverterPane instance.
 * @author Richard
 *
 */
public interface ConverterPaneListener {
	public void closeButtonClicked(ConverterPane pane);
	public void leftTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date);
	public void rightTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date);
	public void onChange();
}
