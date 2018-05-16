package crypto.util;

import java.util.Iterator;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
public class SearchUtil<Currency> {
	private ComboBox<Currency> cmbBox;
	private String filter = "";
	private ObservableList<Currency> items;
	private boolean open = false;
	private int lastSelectedIndex = 0;

	public SearchUtil(ComboBox<Currency> cmbBox) {
		this.cmbBox = cmbBox;
		items = FXCollections.observableArrayList(cmbBox.getItems());
		cmbBox.setTooltip(new Tooltip());
		cmbBox.setOnKeyPressed(this::onKeyPressed);
		cmbBox.setOnHidden(this::handleOnHiding);
		cmbBox.setOnShown(this::handleOnShow);
	}

	public void onKeyPressed(KeyEvent e) {
		if(!open) {
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
			items.filter(el -> el.toString().toLowerCase().contains(item)).forEach(filteredItems::add);
			if(filter.equals("")) {
				showTooltip("Type to filter currencies");
			} else {
				showTooltip(item);
			}
		}
		cmbBox.getItems().setAll(filteredItems);
	}

	public void handleOnHiding(Event e) {
		filter = "";
		open = false;
		cmbBox.getTooltip().hide();
		Currency curr = cmbBox.getSelectionModel().getSelectedItem();
		cmbBox.getItems().setAll(items);
		cmbBox.getSelectionModel().select(curr);
		if(cmbBox.getValue() == null) {
			cmbBox.getSelectionModel().select(lastSelectedIndex);
		}
	}

	public void handleOnShow(Event e) {
		open = true;
		lastSelectedIndex = cmbBox.getSelectionModel().getSelectedIndex();
		
		showTooltip("Type to filter currencies");
	}
	
	private void showTooltip(String text) {
		Window stage = cmbBox.getScene().getWindow();
		double positionX = stage.getX() + cmbBox.localToScene(cmbBox.getBoundsInLocal()).getMinX();
		double positionY = stage.getY() + cmbBox.localToScene(cmbBox.getBoundsInLocal()).getMinY();
		cmbBox.getTooltip().show(stage, positionX, positionY);
		cmbBox.getTooltip().setText(text);
	}
}
