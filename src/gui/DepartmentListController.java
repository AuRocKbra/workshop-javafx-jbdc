package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service;
	private ObservableList<Department> obsList;
	
	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	public void setDeparmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void updateTableView() {
		if(service==null) {
			throw new IllegalStateException("Servico é null!");
		}
		else {
			obsList = FXCollections.observableArrayList(service.findAll());
			tableViewDepartment.setItems(obsList);
		}
	}
	
	@FXML
	public void onBtNewAction() {
		System.out.println("press bt new!");
	}
	
	@Override	
	public void initialize(URL url, ResourceBundle urb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nome"));
		/*Rotina para o table redimentcionar conforme o tamanho da scena*/
		Stage stage = (Stage) Main.getScene().getWindow();//recupera referencia da scena 
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());//atribui as propriedades do stage a tabela
		/*********************************************************************/
	}

}
