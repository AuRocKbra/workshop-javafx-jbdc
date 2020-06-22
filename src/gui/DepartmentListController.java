package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
	public void onBtNewAction(ActionEvent evento) {
		Stage parentStage = gui.util.Utils.currentStage(evento);
		Department dep = new Department();
		createDialogForm(dep,"/gui/DepartmentForm.fxml", parentStage);
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
	
	private void createDialogForm(Department obj,String absoluteName,Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.updateFormData();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Informe dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);//bloqueia o redimencionamento
			dialogStage.initOwner(parentStage);//informa de qual janela ela herda os a exibição
			dialogStage.initModality(Modality.WINDOW_MODAL);//bloqueia a ação para somente a janela em evidência
			dialogStage.showAndWait();
		}catch(IOException e) {
			Alerts.showAlertas("IOException","Erro ao carregar janela",e.getMessage(),AlertType.ERROR);
		}
	}
}
