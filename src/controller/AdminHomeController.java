/**
 * @author manoharchitoda
 * @author surajupadhyay
 */
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

public class AdminHomeController 
{
	@FXML Button listUsers, create, delete, logout;
	
	/**
	 * @param e which ever button is clicked
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent e) throws IOException
	{
		Button b = (Button)e.getSource();
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		BorderPane root;
		
		if(b.equals(logout))
		{
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			root = (BorderPane)loader.load();
			stage.setTitle("Photos");
		}
		else if(b.equals(listUsers))
		{
			loader.setLocation(getClass().getResource("/view/AdminListUser.fxml"));
			root = (BorderPane)loader.load();
			AdminListUserController o = loader.getController();
			o.setUserList();
		}
		else if(b.equals(create))
		{
			loader.setLocation(getClass().getResource("/view/AdminCreateUser.fxml"));
			root = (BorderPane)loader.load();
		}
		//delete button pressed
		else
		{
			loader.setLocation(getClass().getResource("/view/AdminDeleteUser.fxml"));
			root = (BorderPane)loader.load();
		}
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}

}
