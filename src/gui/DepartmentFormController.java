package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	
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
	public void onBtSaveAction() {
		System.out.println("press Save!");
	}
	
	@FXML
	public void onBtCancelAction(){
		System.out.println("press Cancel!");
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
}
