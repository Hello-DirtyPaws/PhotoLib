/**
 * @author manoharchitoda
 * @author surajupadhyay
 */
package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StockPhotosController 
{	
	@FXML Button slideshow, back, copy, logout, search;
	@FXML TextField photoName;
	private Album album;

	@FXML ListView<Photo> listView;
	ObservableList<Photo> items;
	
	/**
	 * 
	 * @throws FileNotFoundException this class might not find the photo
	 */
	public StockPhotosController()throws FileNotFoundException
	{
		//Do Nothing
	}

	/**
	 * @param e source button pressed
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent e) throws IOException
	{
		Button b = (Button)e.getSource();
		FXMLLoader loader = new FXMLLoader();
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		BorderPane root = null;

		if(b.equals(logout))
		{
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			root = (BorderPane)loader.load();
		}
		
		else if(b.equals(back)){
			loader.setLocation(getClass().getResource("/view/StockHome.fxml"));
			root = (BorderPane)loader.load();
			StockHomeController shc = loader.getController();
			shc.init(stage);
		}
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}
	
	public void onSlideshow(ActionEvent event) throws IOException
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
	/**
	 * 
	 * @param event copy action
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onCopy(ActionEvent event) throws IOException
	{
		Photo selectedPhoto = this.listView.getSelectionModel().getSelectedItem();
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
	 * @param stage current stage
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
	
	/**
	 * @param album ~ selected album
	 * @param stage ~ current stage
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void init(Album album, Stage stage) throws IOException
	{
		this.album = album;
		this.items = FXCollections.observableArrayList(this.album.getAllPhotosList());
		listView.setItems(this.items);
		listView.setCellFactory(items -> new PhotoCell());
		this.listView.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent click)
			{

				if (click.getClickCount() == 2)
				{
					try {
						//Use ListView's getSelected Item
						Stage newStage = new Stage();
						Photo currentItemSelected = listView.getSelectionModel().getSelectedItem();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/DisplayPhoto.fxml"));
						BorderPane root = (BorderPane)loader.load();
						DisplayPhotoController dpc = loader.getController();
						dpc.init(currentItemSelected, newStage, album);
						Scene newScene= new Scene(root);
						newStage.setScene(newScene);
						newStage.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println(e.getCause());
					}
				}
			}
		});
	}

	/**
	 * 
	 */
	private class PhotoCell extends ListCell<Photo>
	{
		private ImageView imageView = new ImageView();

		@Override
		protected void updateItem(Photo item, boolean empty)
		{
			super.updateItem(item, empty);
			this.imageView.setFitHeight(50);
			this.imageView.setFitWidth(75);

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
}
