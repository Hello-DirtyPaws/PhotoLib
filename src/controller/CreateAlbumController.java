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

public class CreateAlbumController 
{
	@FXML Button create, cancel;
	@FXML TextField albumName;
	String ap = null;
	
	/**
	 * @param e which ever button is clicked
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent e) throws IOException
	{
		Button b = (Button)e.getSource();
		FXMLLoader loader = new FXMLLoader();
		BorderPane root = null;
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		
		if(b.equals(create))
		{
			if(this.albumName.getText().equalsIgnoreCase(""))
			{
				alert(stage, "Please enter a valid name for an Album.");
				loader.setLocation(getClass().getResource("/view/CreateAlbum.fxml"));
				root = (BorderPane)loader.load();
			}
			else
			{
				if(AllUsers.getCurrUser().createAlbum(albumName.getText()))
				{
					loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
					root = (BorderPane)loader.load();
					UserHomeController uhc = loader.getController();
					uhc.init(stage);
				}
				else
				{
					alert(stage, "Please enter a valid name for an Album.");
					loader.setLocation(getClass().getResource("/view/CreateAlbum.fxml"));
					root = (BorderPane)loader.load();
				}
			}
		}
		else
		{
			loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
			root = (BorderPane)loader.load();
			UserHomeController uhc = loader.getController();
			uhc.init(stage);
		}
		
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}
	
	/**
	 * @param mainstage the primary stage to put the scene
	 * @return boolean true all the time
	 */
	public void alert(Stage mainstage, String message)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainstage);
		alert.setTitle("ERROR!");
		alert.setContentText(message);
		alert.showAndWait();
	}

}
