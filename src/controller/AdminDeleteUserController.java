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

public class AdminDeleteUserController 
{
	@FXML Button cancel, delete;
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
		
		if(b.equals(delete))
		{
			String userName = username.getText().trim().toLowerCase();
			if(userName.equals(null) || userName.equals(""))
			{
				alert_unsuccess(stage, "Sorry! Invalid Entry!");
				loader.setLocation(getClass().getResource("/view/AdminDeleteUser.fxml"));
			}
			
			else if(userName.equals("admin") || userName.equals("stock"))
			{
				alert_unsuccess(stage, "Sorry! Admin or Stock can not be deleted!");
				loader.setLocation(getClass().getResource("/view/AdminDeleteUser.fxml"));
			}
			else
			{
				AllUsers u = AllUsers.getAllUsersObj();
				if(u.removeUser(userName))
				{
					alert_success(stage, userName);
					loader.setLocation(getClass().getResource("/view/AdminHome.fxml"));
				}
				else
				{
					alert_unsuccess(stage, "Sorry! No User with username : " + userName);
					loader.setLocation(getClass().getResource("/view/AdminDeleteUser.fxml"));
				}
			}
		}		
		//cancel button pressed
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
	 * @param mainstage the stage to display the scene
	 * @param content, message to display
	 */
	private void alert_unsuccess(Stage mainstage, String content)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainstage);
		alert.setTitle("Delete User");
		alert.setHeaderText("Deleting Error!");
		alert.setContentText(content);
		alert.showAndWait();
	}

	/**
	 * 
	 * @param mainstage the stage to display the scene
	 * @param username, used to display the user name
	 */
	private void alert_success(Stage mainstage, String username)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Delete User");
		alert.setHeaderText("Successfully deleted User : " + username);
		alert.showAndWait();
	}

}
