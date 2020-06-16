package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;

	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("menu Seller");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml",(DepartmentListController controller)->{
			controller.setDeparmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml",x->{});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			Scene mainScene = Main.getScene();//obtem a tela principal
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();//isola a vbox da tela principal
			Node mainMenu = mainVbox.getChildren().get(0);//salva o primeiro conteúdo da vbox da tela principal
			mainVbox.getChildren().clear();//limpa todo o conteúdo da tela
			mainVbox.getChildren().add(mainMenu);//adiciona o conteúdo salvo
			mainVbox.getChildren().addAll(newVbox);//adiciona o conteúdo da vbox referente a opção selecionada
			
			/*Excuta função generica passada por parametro ao invocar o metodo*/
			T controller = loader.getController();
			initializingAction.accept(controller);
			/*************************************************/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Alerts.showAlertas("Error","Erro de carregamento",e.getMessage(),AlertType.ERROR);
		}
		
	}
}
