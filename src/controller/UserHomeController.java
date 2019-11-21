/**
 * @author manoharchitoda
 * @author surajupadhyay
 */
package controller;


import java.io.IOException;
import java.util.Optional; 
import app.Album;
import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.AllUsers;

public class UserHomeController 
{
	@FXML Button copy, rename, move, paste, create, delete, logout, searchPhoto;
	@FXML ListView<Album> listview;
	ObservableList<Album> albums;
	
	/**
	 * @param event source button pressed
	 * @throws IOException loader might not load the requested FXML
	 */
	public void onClicked(ActionEvent event) throws IOException
	{
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		BorderPane root = null;
		
		if(b.equals(logout))
		{
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			root = (BorderPane)loader.load();
			stage.setTitle("Photos");
			Album.setCopiedPhoto(null);
			Album.setCopiedPhotoFrom(null);
		}
		else if(b.equals(create))
		{
			loader.setLocation(getClass().getResource("/view/CreateAlbum.fxml"));
			root = (BorderPane)loader.load();
		}
		else if(b.equals(rename)) 
		{
			if(this.listview.getSelectionModel().getSelectedItem() != null)
			{
				loader.setLocation(getClass().getResource("/view/RenameAlbum.fxml"));
				root = (BorderPane)loader.load();
				RenameAlbumController rac = loader.getController();
				rac.init(this.listview.getSelectionModel().getSelectedItem());
			}
			else
			{
				loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
				root = (BorderPane)loader.load();	
				displayMessage(stage, "Sorry! No album to rename!");
				UserHomeController ush = loader.getController();			
				ush.init(stage);
			}
		}
		else if(b.equals(delete))
		{
			Album albumToDelete = this.listview.getSelectionModel().getSelectedItem();
			if(albumToDelete != null)
			{		
				if(deleteConfirm(stage))
					AllUsers.getCurrUser().deleteAlbum(albumToDelete.getName());
			}
			else
			{
				displayMessage(stage, "Sorry! No album to delete!");
			}
			loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
			root = (BorderPane)loader.load();	
			UserHomeController ush = loader.getController();			
			ush.init(stage);
		}
		//search photo button pressed
		else
		{
			loader.setLocation(getClass().getResource("/view/SearchPhotos.fxml"));
			root = (BorderPane)loader.load();	
		}
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}

	/**
	 * @param event an action of copy
	 * @throws IOException loader might not load the requested FXML
	 */
	public void onCopy(ActionEvent event) throws IOException
	{
		User user = AllUsers.getCurrUser();
		Album album = this.listview.getSelectionModel().getSelectedItem();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		if(album != null)
		{
			user.copyAlbum(album.getName());
			displayMessage(stage, "Album copied successully!");
		}
		else
		{
			displayMessage(stage, "Sorry! No album to copy!");
		}
		
	}
	
	/**
	 * @param event ~ an action of paste
	 * @throws IOException ~ loader might not load the requested FXML
	 */
	public void onPaste(ActionEvent event) throws IOException
	{
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		if(User.getCopiedAlbum() != null)
		{	
			if(AllUsers.getCurrUser().pasteAlbum())
			{
				displayMessage(stage, "Album pasted successully!");
			}
			else
			{
				displayMessage(stage, "Album Already exist!");
			}
		}
		else
		{
			displayMessage(stage, "Sorry! Nothing to Paste!");
		}
		this.init(stage);
	}
	
	/**
	 * @param stage ~ The current stage
	 * @throws IOException ~ loader might not load the requested FXML
	 */
 	public void init(Stage stage)throws IOException 
	{
		if(stage != null) 
		{
			stage.setTitle("Photos/" + AllUsers.getCurrUser().getUsername());
			this.albums = FXCollections.observableArrayList(AllUsers.getCurrUser().getAllAlbumsList());
			this.listview.getItems().setAll(this.albums);
			this.listview.getSelectionModel().selectFirst();
			
			this.listview.setOnMouseClicked(new EventHandler<MouseEvent>() 
			{
				@Override
				public void handle(MouseEvent click) 
				{

			        if (click.getClickCount() == 2)
			        {
			        	//Use ListView's getSelected Item
			            Album currentItemSelected = listview.getSelectionModel().getSelectedItem();
			            stage.setTitle(stage.getTitle() + "/" + currentItemSelected.getName());
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/UserPhotos.fxml"));
						BorderPane root = null;
						try 
						{
							root = (BorderPane)loader.load();
							UserPhotosController upc = loader.getController();
							upc.init(currentItemSelected, stage);
							Scene newScene= new Scene(root);
							stage.setScene(newScene);
							stage.show();
						} catch (IOException e) 
						{
							e.printStackTrace();
						}
			        }
			    }
			});
		}
	}
	
	/**
	 * @param mainstage ~ current stage
	 * @return boolean ~ what the user chooses
	 */
	public boolean deleteConfirm(Stage mainstage)
	{
		boolean choice = false;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Deleting");
		alert.setHeaderText("Deleting this Album!");
		String content = "Are you sure you want delete this album?";
		alert.setContentText(content);
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK)
			choice = true;
		else
			choice = false;
		return choice;
	}

	/**
	 * 
	 * @param stage ~ the new stage
	 * @param message ~ the display information to show
	 */
	public void displayMessage(Stage stage, String message)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(stage);
		alert.setTitle("Copy/Paste Album");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

}
