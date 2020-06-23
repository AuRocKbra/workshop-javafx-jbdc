package gui;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegratyException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable,DataChangeListener{
	
	private SellerService service;
	private ObservableList<Seller> obsList;
	
	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, Seller>tableColumnEDIT;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	@FXML
	private Button btNew;
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void updateTableView() {
		if(service==null) {
			throw new IllegalStateException("Servico é null!");
		}
		else {
			obsList = FXCollections.observableArrayList(service.findAll());
			tableViewSeller.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}
	}
	
	@FXML
	public void onBtNewAction(ActionEvent evento) {
		Stage parentStage = gui.util.Utils.currentStage(evento);
		Seller dep = new Seller();
		createDialogForm(dep,"/gui/SellerForm.fxml", parentStage);
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
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());//atribui as propriedades do stage a tabela
		/*********************************************************************/
	}
	
	private void createDialogForm(Seller obj,String absoluteName,Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//			SellerFormController controller = loader.getController();
//			controller.setSeller(obj);
//			controller.setSellerService(new SellerService());
//			controller.subcribeDataChangeListener(this);
//			controller.updateFormData();
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Informe dados do departamento");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);//bloqueia o redimencionamento
//			dialogStage.initOwner(parentStage);//informa de qual janela ela herda os a exibição
//			dialogStage.initModality(Modality.WINDOW_MODAL);//bloqueia a ação para somente a janela em evidência
//			dialogStage.showAndWait();
//		}catch(IOException e) {
//			Alerts.showAlertas("IOException","Erro ao carregar janela",e.getMessage(),AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChange() {
		updateTableView();
	}
	
	private void initEditButtons() {//cria e atribui a função aos botões de edit de cada celula
		tableColumnEDIT.setCellValueFactory(param->new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param->new TableCell<Seller,Seller>(){
			private final Button button = new Button("Edit");
			
			@Override
			protected void updateItem(Seller obj,boolean empty) {
				super.updateItem(obj,empty);
				
				if(obj==null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event->createDialogForm(obj,"/gui/SellerForm.fxml",Utils.currentStage(event)));
			}
		});
	}
	
	public void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param->new TableCell<Seller,Seller>(){
			private final Button button = new Button("Remove");
			
			@Override
			protected void updateItem(Seller obj,boolean empty) {
				if(obj==null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event->removeEntity(obj));
			}
		});
	}

	private void  removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação","Deseja realmente deletar o registro?");
		if(result.get()==ButtonType.OK) {
			if(service ==null) {
				throw new IllegalStateException("Serviço não instanciado!");
			}
			try {
				service.remove(obj);
				updateTableView();
			}catch(DBIntegratyException e) {
				Alerts.showAlertas("Erro", "Erro de remoção de dados",e.getMessage(),AlertType.ERROR);
			}
		}
	}
}