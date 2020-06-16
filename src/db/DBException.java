package db;

import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;

public class DBException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DBException(String msg) {
		Alerts.showAlertas("Error",null,msg,AlertType.ERROR);
	}
}
