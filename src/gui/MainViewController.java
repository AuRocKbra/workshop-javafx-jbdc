package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
		loadView("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			Scene mainScene = Main.getScene();//obtem a tela principal
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();//isola a vbox da tela principal
			Node mainMenu = mainVbox.getChildren().get(0);//salva o primeiro conteúdo da vbox da tela principal
			mainVbox.getChildren().clear();//limpa todo o conteúdo da tela
			mainVbox.getChildren().add(mainMenu);//adiciona o conteúdo salvo
			mainVbox.getChildren().addAll(newVbox);//adiciona o conteúdo da vbox referente a opção selecionada
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Alerts.showAlertas("Error","Erro de carregamento",e.getMessage(),AlertType.ERROR);
		}
		
	}
	
}
