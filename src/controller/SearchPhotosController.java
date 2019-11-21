package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SearchPhotosController 
{
	@FXML Button searchByDate, searchBy_1_Tag, searchBy_2_Tags, cancel;
	
	
	/**
	 * 
	 * @param event search action
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onSearch(ActionEvent event) throws IOException
	{
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		BorderPane root = null;
		
		if(b.equals(searchByDate)) 
		{
			loader.setLocation(getClass().getResource("/view/SearchByDate.fxml"));
			root = (BorderPane)loader.load();
		}
		else if(b.equals(searchBy_1_Tag))
		{
			loader.setLocation(getClass().getResource("/view/SearchByOne.fxml"));
			root = (BorderPane)loader.load();
		}
		//searchBy_2_Tags
		else
		{
			loader.setLocation(getClass().getResource("/view/SearchByTwo.fxml"));
			root = (BorderPane)loader.load();
			((SearchByTwoController)loader.getController()).init();
		}
		
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}

	/**
	 * 
	 * @param event cancel action
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onCancel(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
		BorderPane root = (BorderPane)loader.load();
		
		UserHomeController ush = loader.getController();			
		ush.init(stage);
		
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();		
	}
	
}
