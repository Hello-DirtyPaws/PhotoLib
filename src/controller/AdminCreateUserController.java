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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.AllUsers;

public class AdminCreateUserController 
{
	@FXML Button cancel, create;
	@FXML TextField username;
	
	/**
	 * @param e which ever button is clicked
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent e) throws IOException
	{
		Button b = (Button)e.getSource();
		FXMLLoader loader = new FXMLLoader();
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();

		if(b.equals(create))
		{
			String userName = username.getText().trim().toLowerCase();
			if(userName.equalsIgnoreCase("admin") || userName.equalsIgnoreCase("stock"))
			{
				alert_unsuccess(stage, "Sorry! Cannot create a user with username Admin or Stock!");	
				loader.setLocation(getClass().getResource("/view/AdminCreateUser.fxml"));
			}
			else if(userName.equalsIgnoreCase("") || userName.equalsIgnoreCase(null))
			{
				alert_unsuccess(stage, "Sorry! Cannot create a user with no username!");	
				loader.setLocation(getClass().getResource("/view/AdminCreateUser.fxml"));
			}
			else
			{
				AllUsers u = AllUsers.getAllUsersObj();
				if(u.addUser(userName))
				{
					alert_success(stage, userName);						
					loader.setLocation(getClass().getResource("/view/AdminHome.fxml"));
				}
				else
				{
					alert_unsuccess(stage, "Sorry! User \"" + userName + "\" already exist!");	
					loader.setLocation(getClass().getResource("/view/AdminCreateUser.fxml"));
				}
			}
		}
		//Cancel button pressed
		else
		{
			loader.setLocation(getClass().getResource("/view/AdminHome.fxml"));
		}
		
		BorderPane root = (BorderPane)loader.load();
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}
	
	/**
	 * 
	 * @param mainstage the stage to hold the scene
	 * @param content, message to display
	 */
	private void alert_unsuccess(Stage mainstage, String content)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainstage);
		alert.setTitle("Create User");
		alert.setHeaderText("Error in Creating User!");
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * 
	 * @param mainstage the stage to hold the scene
	 * @param username, used to display the username
	 */
	private void alert_success(Stage mainstage, String username)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Create User");
		alert.setHeaderText("Successfully created User : " + username);
		alert.showAndWait();
	}
}
