package crypto.client;

import crypto.client.model.Currency;

import java.time.LocalDate;

/**
 * An interface for callbacks from a ConverterPane instance.
 * @author Richard
 *
 */
public interface ConverterPaneListener {
	public void closeButtonClicked(ConverterPane pane);
	public void leftTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date);
	public void rightTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date);
	public void moveUp(ConverterPane pane);
	public void moveDown(ConverterPane pane);
	public void onChange();
}
