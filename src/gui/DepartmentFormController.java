package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity==null) {
			throw new IllegalStateException("Objeto departamento não instanciado!");
		}
		if(service ==null) {
			throw new IllegalStateException("Objeto serviço não instanciado!");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
		}catch(DBException e) {
			Alerts.showAlertas("Error", "Operação de salvar dados",e.getMessage(),AlertType.ERROR);
		}
		gui.util.Utils.currentStage(event).close();
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}
	}

	private Department getFormData() {
		Department obj = new Department();
		obj.setId(gui.util.Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event){
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializedNodes();
	}
	
	private void initializedNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void setDepartment(Department entity) {
		this.entity=entity;
	}
	
	public void updateFormData() {
		if(entity==null) {
			throw new IllegalStateException("Departamento não instanciado!");
		}
		if(entity.getId()==null) {
			txtId.setText("");
		}
		else {
			txtId.setText(String.valueOf(entity.getId()));
		}
		txtNome.setText(entity.getNome());
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subcribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
}
