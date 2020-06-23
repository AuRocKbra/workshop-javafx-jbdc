package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	private Seller entity;
	
	private SellerService service;
	
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
			throw new IllegalStateException("Objeto vendedor não instanciado!");
		}
		if(service ==null) {
			throw new IllegalStateException("Objeto serviço não instanciado!");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			gui.util.Utils.currentStage(event).close();
		}catch(ValidationException e) {
			setErrorsMessages(e.getErrors());
		}
		catch(DBException e) {
			Alerts.showAlertas("Error", "Operação de salvar dados",e.getMessage(),AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException exception = new ValidationException("Erro de validação");
		obj.setId(gui.util.Utils.tryParseToInt(txtId.getText()));
		if(txtNome.getText()==null || txtNome.getText().trim().equals("")) {
			exception.addError("Nome"," <-Este campo não pode ser vazio!");
		}
		obj.setNome(txtNome.getText());
		if(exception.getErrors().size()>0) {
			throw exception;
		}
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
	
	public void setSeller(Seller entity) {
		this.entity=entity;
	}
	
	public void updateFormData() {
		if(entity==null) {
			throw new IllegalStateException("Vendedor não instanciado!");
		}
		if(entity.getId()==null) {
			txtId.setText("");
		}
		else {
			txtId.setText(String.valueOf(entity.getId()));
		}
		txtNome.setText(entity.getNome());
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void subcribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	private void setErrorsMessages(Map<String,String> erros) {
		Set<String> fields = erros.keySet();
		if(fields.contains("Nome")) {
			labelErrorName.setText(erros.get("Nome"));
		}
	}
}
