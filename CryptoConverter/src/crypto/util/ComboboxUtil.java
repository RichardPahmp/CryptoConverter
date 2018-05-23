package crypto.util;

import crypto.client.model.Currency;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ComboboxUtil {
	
	public static void setupCombobox(ComboBox<Currency> cmbBox) {
		cmbBox.setCellFactory(createCellFactory());
		cmbBox.setButtonCell(createListCell());
		new SearchUtil(cmbBox);
	}
	
	private static Callback<ListView<Currency>, ListCell<Currency>> createCellFactory() {
		return new Callback<ListView<Currency>, ListCell<Currency>>() {
			@Override
			public ListCell<Currency> call(ListView<Currency> arg0) {
				return new ListCell<Currency>() {
					@Override
					protected void updateItem(Currency item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null && !empty) {
							setText(item.getCoinFullName());
						}
					}
				};
			}
		};
	}

	private static ListCell<Currency> createListCell() {
		return new ListCell<Currency>() {
			protected void updateItem(Currency item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null && !empty) {
					setText(item.getCoinName());
				}
			}

			;
		};
	}
	
}
