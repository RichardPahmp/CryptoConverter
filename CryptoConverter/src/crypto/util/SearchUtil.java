package crypto.util;

import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

/**
 * 
 * @author Emil Ögge
 *
 * @param <Currency>
 */
public class SearchUtil<Currency> {
	private ComboBox<Currency> cmbBox;
	String filter = "";
	private ObservableList<Currency> items;

	public SearchUtil(ComboBox<Currency> cmbBox) {
		this.cmbBox = cmbBox;
		items = FXCollections.observableArrayList(cmbBox.getItems());
		cmbBox.setTooltip(new Tooltip());
		cmbBox.setOnKeyPressed(this::onKeyPressed);
		cmbBox.setOnHidden(this::handleOnHiding);
	}

	public void onKeyPressed(KeyEvent e) {
		ObservableList<Currency> filteredItems = FXCollections.observableArrayList();
		KeyCode code = e.getCode();
		
		if(code.isLetterKey()) {
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
		
		if (code == KeyCode.TAB) {
			filter = "";
			cmbBox.getItems().setAll(items);
		}
		
		if (filter.length() == 0) {
			filteredItems = items;
			cmbBox.getTooltip().hide();
		} 
		
		else {
			Stream<Currency> items = cmbBox.getItems().stream();
			String item = filter.toString().toLowerCase();
			items.filter(el -> el.toString().toLowerCase().contains(item)).forEach(filteredItems::add);
			cmbBox.getTooltip().setText(item);
			Window stage = cmbBox.getScene().getWindow();	
			
			double positionX = stage.getX() + cmbBox.localToScene(cmbBox.getBoundsInLocal()).getMinX();
			double positionY = stage.getY() + cmbBox.localToScene(cmbBox.getBoundsInLocal()).getMinY();
			
			cmbBox.getTooltip().show(stage, positionX, positionY);
			cmbBox.show();
		}
		cmbBox.getItems().setAll(filteredItems);
	}
	
	public void handleOnHiding(Event e) {
		filter = "";
		cmbBox.getTooltip().hide();
		Currency curr = cmbBox.getSelectionModel().getSelectedItem();
		cmbBox.getItems().setAll(items);
		cmbBox.getSelectionModel().select(curr);
	}
}
