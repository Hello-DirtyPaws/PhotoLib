package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import app.Album;
import app.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AllUsers;

/**
 * @author manoharchitoda
 * @author surajupadhyay
 */
public class UserPhotosController 
{
	@FXML Button slideshow, back, move, copy, paste, add, delete, logout, edit,
	searchByDate, searchBy_1_Tag, searchBy_2_Tags;
	@FXML TextField photoName;
	@FXML ListView<Photo> listview;
	ObservableList<Photo> pics;
	public Album album;

	/**
	 * @param album ~ selected album
	 * @param stage ~ current stage
	 * @throws IOException ~ loader might not load the requested FXML
	 */
	public void init(Album album, Stage stage) throws IOException
	{
		this.album = album;
		this.pics = FXCollections.observableArrayList(album.getAllPhotosList());
		this.listview.setItems(this.pics);
		this.listview.getSelectionModel().selectFirst();
		this.listview.setCellFactory(pics -> new PhotoCell());
		this.listview.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent click)
			{
				if (click.getClickCount() == 2)
				{
					try {
						//Use ListView's getSelected Item
						Stage newStage = new Stage();
						Photo currentItemSelected = listview.getSelectionModel().getSelectedItem();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/DisplayPhoto.fxml"));
						BorderPane root = (BorderPane)loader.load();
						DisplayPhotoController dpc = loader.getController();
						dpc.init(currentItemSelected, newStage, album);
						Scene newScene= new Scene(root);
						newStage.setScene(newScene);
						newStage.show();
					} catch (IOException e) {
						System.out.println(e.getCause());
					}
				}
			}
		});
	}

	/**
	 * @param <Calender> uses a calendar
	 * @param event source button pressed
	 * @throws IOException the loader might not be able to loader the FXML
	 */
	public <Calender> void onClicked(ActionEvent event) throws IOException
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
		else if(b.equals(add)) 
		{
			FileChooser fc = new FileChooser();
			File selected = fc.showOpenDialog(null);
			if(selected != null) 
			{
				String caption = inputCaption(stage);
				Calendar c = Calendar.getInstance();
				c.setTime(new Date(selected.lastModified()));

				if(AllUsers.getCurrUser().getAlbum(album.getName()).
						addPhoto(new Photo(c, caption, selected.getAbsolutePath())))
				{
					displayMessage(stage, "Photo added successfull!");
				}
				else
				{
					displayMessage(stage, "Photo already exist!");
				}
			}
			loader.setLocation(getClass().getResource("/view/UserPhotos.fxml"));
			root = (BorderPane)loader.load();
			UserPhotosController upc = loader.getController();
			upc.init(album, stage);
		}
		else if(b.equals(delete))
		{
			Photo selectedPhoto = this.listview.getSelectionModel().getSelectedItem();
			if(selectedPhoto != null)
			{
				if(alert(stage))
				{
					this.album.deletePhoto(this.listview.getSelectionModel().getSelectedItem().getPath());
				}
			}
			else
			{
				displayMessage(stage, "Sorry! No photo to delete!");
			}
			loader.setLocation(getClass().getResource("/view/UserPhotos.fxml"));
			root = (BorderPane)loader.load();
			UserPhotosController upc = loader.getController();
			upc.init(album, stage);
		}		
		else if(b.equals(back))
		{
			loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
			root = (BorderPane)loader.load();
			UserHomeController shc = loader.getController();
			shc.init(stage);
		}
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}

	/**
	 * @author manoharchitoda
	 */
	private class PhotoCell extends ListCell<Photo>
	{
		private ImageView imageView = new ImageView();

		@Override
		protected void updateItem(Photo item, boolean empty)
		{
			super.updateItem(item, empty);
			this.imageView.setFitHeight(150);
			this.imageView.setFitWidth(200);

			if (empty || item == null) {
				imageView.setImage(null);
				setGraphic(null);
				setText(null);
			} 
			else
			{	
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(item.getPath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				imageView.setImage(new Image(fis));
				setText(item.getCaption());

				setGraphic(imageView);
			}
		}
	}

	/**
	 * @param mainStage ~current stage
	 * @return String ~what the user enters
	 */
	private String inputCaption(Stage mainStage)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Photo Caption");
		dialog.setHeaderText("Caption please!");
		dialog.setContentText("Please enter the caption for this photo:");
		Optional<String> result = dialog.showAndWait();
		String str = result.toString().substring(9,result.toString().length()-1);
		return str;
	}

	/**
	 * 
	 * @param mainstage ~ current stage
	 * @return boolean ~ what the user chooses
	 */
	private boolean alert(Stage mainstage)
	{
		boolean choice = false;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(mainstage);
		alert.setTitle("Deleting");
		alert.setHeaderText("Deleting this Photo!");
		String content = "Are you sure you want delete this photo?";
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
	 * @param event ~ an action performed
	 * @throws IOException ~ the loader might no load requested FXML
	 */
	public void onShift(ActionEvent event) throws IOException
	{
		Photo selectedPhoto = this.listview.getSelectionModel().getSelectedItem();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

		if(selectedPhoto != null)
		{

			if(((Button)event.getSource()).equals(move)) 
				this.album.movePhoto(selectedPhoto.getPath());
			else 
				this.album.copyPhoto(selectedPhoto.getPath());

			displayMessage(stage, "Photo captured successully!");
		}
		else
		{
			displayMessage(stage, "Sorry! No photo to capture!");
		}

	}

	/**
	 * 
	 * @param event ~ an action performed
	 * @throws IOException ~ the loader might no load requested FXML
	 */
	public void onPaste(ActionEvent event) throws IOException
	{
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();		
		int result = this.album.pastePhoto();
		switch(result)
		{
		case 0: 
			displayMessage(stage, "Photo pasted successully!");
			break;
		case 1: 
			displayMessage(stage, "Photo Already exist!");
			break;
		case -1:
			displayMessage(stage, "Sorry! No photo to Paste!");
			break;
		}
		this.init(album, stage);
	}


	/**
	 * @param event ~ an action performed
	 * @throws IOException ~ the loader might no load requested FXML
	 */
	public void onSlideshow(ActionEvent event) throws IOException
	{
		if(this.pics.size() > 0)
		{
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Slideshow.fxml"));
			BorderPane root = (BorderPane)loader.load();
			SlideshowController sc = loader.getController();
			sc.init(this.album);
			Scene newScene= new Scene(root);
			newStage.setScene(newScene);
			newStage.show();
		}
		else
		{
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			displayMessage(stage, "No photo for slideshow!");
		}
	}

	/**
	 * @param event ~ an action performed
	 * @throws IOException ~ the loader might no load requested FXML
	 */
	public void onEdit(ActionEvent event)throws IOException
	{
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Photo photo = listview.getSelectionModel().getSelectedItem();		
		if(photo != null)
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/EditPhoto.fxml"));
			BorderPane root = (BorderPane)loader.load();

			EditPhotoController shc = loader.getController();
			shc.init(album, photo);

			Scene newScene= new Scene(root);
			stage.setScene(newScene);
			stage.show();
		}
		else
		{
			displayMessage(stage, "Sorry! No photo to edit!");
		}
	}
	
	/**
	 * 
	 * @param stage ~THIS STAGE
	 * @param message, the display information to show
	 */
	public void displayMessage(Stage stage, String message)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(stage);
		alert.setTitle("Copy/Paste Photo");
		alert.setHeaderText(message);
		alert.showAndWait();
	}


}
