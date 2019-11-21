package controller;
import java.io.IOException;

import app.Album;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.AllUsers;
/**
 * @author manoharchitoda
 * @author surajupadhyay
*/
public class RenameAlbumController 
{
	@FXML Button rename, cancel;
	@FXML TextField albumName;
	private Album album;
	/**
	 * @param event click action
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClick(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		BorderPane root = (BorderPane)loader.load();
		
		if(event.getSource().equals(rename))
			AllUsers.getCurrUser().editAlbumName(album.getName(), albumName.getText());
			
		//on cancel do nothing --> just go back --> reload User Home Page
		UserHomeController uhc = loader.getController();
		uhc.init(stage);
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}

	/**
	 * @param album ~ current album
	 */
	public void init(Album album)
	{
		this.album = album;
	}
}
