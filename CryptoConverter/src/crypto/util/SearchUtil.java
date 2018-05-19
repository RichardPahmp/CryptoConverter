package crypto.util;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import crypto.client.model.Currency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

import java.util.stream.Stream;

/**
 * 
 * @author Emil Ögge
 *
 * @param <Currency>
 */
public class SearchUtil {
	private ComboBox<Currency> cmbBox;
	private String filter = "";
	private ObservableList<Currency> items;
	private boolean open = false;
	private int lastSelectedIndex = 0;

	/**
	 * Makes the given combobox searchable.
	 * 
	 * @param cmbBox
	 */
	public SearchUtil(ComboBox<Currency> cmbBox) {
		this.cmbBox = cmbBox;
		items = FXCollections.observableArrayList(cmbBox.getItems());
		cmbBox.setTooltip(new Tooltip());
		cmbBox.setOnKeyPressed(this::onKeyPressed);
		cmbBox.setOnHidden(this::handleOnHiding);
		cmbBox.setOnShown(this::handleOnShow);
	}

	/**
	 * Handle keyboard input
	 * 
	 * @param e
	 */
	public void onKeyPressed(KeyEvent e) {
		if (!open) {
			return;
		}

		ObservableList<Currency> filteredItems = FXCollections.observableArrayList();
		KeyCode code = e.getCode();

		if (code.isLetterKey()) {
			filter = filter + e.getText();
		}

		if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
			filter = filter.substring(0, filter.length() - 1);
			cmbBox.getItems().setAll(items);
		}

		if (code == KeyCode.ESCAPE) {
			filter = "";
		}

		if (code == KeyCode.ENTER) {
			filter = "";
			cmbBox.getItems().setAll(items);
		}

		if (filter.length() == 0) {
			filteredItems = items;
			showTooltip("Type to filter currencies");
		} else {
			Stream<Currency> items = cmbBox.getItems().stream();
			String item = filter.toString().toLowerCase();
			items.filter(
					c -> c.getSymbol().toLowerCase().contains(item) || c.getCoinName().toLowerCase().contains(item))
					.forEach(filteredItems::add);
			if (filter.equals("")) {
				showTooltip("Type to filter currencies");
			} else {
				showTooltip(item);
			}
		}
		cmbBox.getItems().setAll(filteredItems);
	}

	/**
	 * Hide the tooltip when the combobox is hidden.
	 * 
	 * @param e
	 */
	public void handleOnHiding(Event e) {
		filter = "";
		open = false;
		cmbBox.getTooltip().hide();
		Currency curr = cmbBox.getSelectionModel().getSelectedItem();
		cmbBox.getItems().setAll(items);
		cmbBox.getSelectionModel().select(curr);
		if (cmbBox.getValue() == null) {
			cmbBox.getSelectionModel().select(lastSelectedIndex);
		}
	}

	/**
	 * Show the tooltip when the combobox is shown.
	 * 
	 * @param e
	 */
	public void handleOnShow(Event e) {
		open = true;
		lastSelectedIndex = cmbBox.getSelectionModel().getSelectedIndex();

		showTooltip("Type to filter currencies");
	}

	/**
	 * Shows the tooltip to the screen with the given text.
	 * 
	 * @param text
	 */
	private void showTooltip(String text) {
		Window stage = cmbBox.getScene().getWindow();
		double positionX = stage.getX() + cmbBox.localToScene(cmbBox.getBoundsInLocal()).getMinX();
		double positionY = stage.getY() + cmbBox.localToScene(cmbBox.getBoundsInLocal()).getMinY();
		cmbBox.getTooltip().show(stage, positionX, positionY);
		cmbBox.getTooltip().setText(text);
	}
}
