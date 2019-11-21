/**
 * @author manoharchitoda
 * @author surajupadhyay
 */
package controller;
import java.io.IOException;

import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.AllUsers;

public class AdminListUserController 
{
	@FXML Button cancel, logout;
	@FXML ListView<User> userlist;
	public static ObservableList<User> obs;
	
 	/**
 	 * sets the users list
 	 */
 	public void setUserList()
 	{
 		AllUsers u = AllUsers.getAllUsersObj();
 		obs = FXCollections.observableArrayList(u.getUsersList());
 		if(obs.isEmpty())
 			System.out.println("Hello");
 		userlist.setItems(obs);
 	}
	
	/**
	 * @param e which ever button is clicked
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent e) throws IOException
	{
		Button b = (Button)e.getSource();
		FXMLLoader loader = new FXMLLoader();
		
		if(b.equals(logout))
		{
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
		}
		//cancel button pressed
		else
		{
			loader.setLocation(getClass().getResource("/view/AdminHome.fxml"));
		}
		
		BorderPane root = (BorderPane)loader.load();
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}

}
