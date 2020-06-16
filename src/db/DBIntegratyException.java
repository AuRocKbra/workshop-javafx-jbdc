package db;

import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;

public class DBIntegratyException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DBIntegratyException(String msg) {
		Alerts.showAlertas("Error", null, msg, AlertType.ERROR);
	}
}
