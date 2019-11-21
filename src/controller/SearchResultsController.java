package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.AllUsers;

public class SearchResultsController 
{
	@FXML Button create, slideshow, copy, back;
	@FXML ListView<Photo> listview;
	ObservableList<Photo> pics;
	private Album album;

	public void init(Album album)
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
	 * @param event clicked action
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent event) throws IOException
	{
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		BorderPane root = null;

		if(b.equals(back)) 
		{
			loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
			root = (BorderPane)loader.load();
			((UserHomeController)loader.getController()).init(stage);
		}

		else if(b.equals(create))
		{
			int len = pics.size();
			String albumname = input(stage);
			AllUsers.getCurrUser().createAlbum(albumname);
			this.listview.getSelectionModel().selectFirst();
			for (int i = 0; i < len; i++)
			{
				AllUsers.getCurrUser().getAlbum(albumname).addPhoto(this.listview.getSelectionModel().getSelectedItem());
				this.listview.getSelectionModel().selectNext();
			}
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
	 * 
	 * @param event copy action
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onCopy(ActionEvent event) throws IOException
	{
		Photo selectedPhoto = this.listview.getSelectionModel().getSelectedItem();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

		if(selectedPhoto != null)
		{
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
	 * @param stage ~ Current stage
	 * @param message ~ the display information to show
	 */
	public void displayMessage(Stage stage, String message)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(stage);
		alert.setTitle("Copy/Paste Photo");
		alert.setHeaderText(message);
		alert.showAndWait();
	}
	

	/**
	 * @param mainStage ~current stage
	 * @return String ~what the user enters
	 */
	private String input(Stage mainStage)
	{
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("CREATE ALBUM");
		dialog.setHeaderText("ALBUM NAME!");
		dialog.setContentText("Please enter the name for this album:");
		Optional<String> result = dialog.showAndWait();
		String str = result.toString().substring(9,result.toString().length()-1);
		return str;
	}

}
