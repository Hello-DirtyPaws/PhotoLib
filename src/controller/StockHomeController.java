/**
 * @author manoharchitoda
 * @author surajupadhyay
 */
package controller;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import app.Album;
import app.Photo;
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
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StockHomeController 
{
	@FXML Button copy, logout;
	@FXML ListView<Album> listview;
	String [] images = {"Stock/Glass_Bottle.jpg","Stock/lego.jpg","Stock/penguin.jpg",
			"Stock/simple.jpg","Stock/simple2.jpg"};
	Album stock = new Album("Stock");
	private ObservableList<Album> items;
	
	/**
	 * @param e source button pressed
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent e) throws IOException
	{
		Button b = (Button)e.getSource();
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader();
		
		if(b.equals(logout))
		{
			loader.setLocation(getClass().getResource("/view/Login.fxml"));
			stage.setTitle("Photos");
			BorderPane root = (BorderPane)loader.load();
			Scene newScene= new Scene(root);
			stage.setScene(newScene);
			stage.show();
		}
		//copy album clicked
		else
		{			
			if(stock != null)
			{
				User.setCopiedAlbum(new Album(stock));
				displayMessage(stage, "Album copied successully!");
			}
			else
			{
				displayMessage(stage, "Sorry! Nothing to copy!");
			}
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
		alert.setTitle("Copy/Paste Album");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	
	/**
	 * @param stage current stage
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void init(Stage stage)throws IOException 
	{
		initPhotos();
		stage.setTitle("Photos/Stock/");
		this.items = FXCollections.observableArrayList(stock);
		this.listview.setItems(this.items);
		this.listview.getSelectionModel().selectFirst();
		this.listview.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2)
		        {
		        	stage.setTitle("Photos/Stock/Stock/");
		        	
		            //Use ListView's getSelected Item
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/StockPhotos.fxml"));
					try {
						BorderPane root = (BorderPane)loader.load();
						StockPhotosController spc = loader.getController();
						spc.init(stock, stage);
						Scene newScene= new Scene(root);
						stage.setScene(newScene);
						stage.show();
					} catch (Exception e) {
						e.printStackTrace();
						//System.out.println(e.getCause().getStackTrace());
						//do nothing
					}
		        }
		    }
		});
	}
	
	/**
	 * initialises the default stock photos
	 */
	public void initPhotos()
	{
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < images.length; i++)
		{
			File f = new File(this.images[i]);
			c.setTime(new Date(f.getAbsoluteFile().lastModified()));
			this.stock.addPhoto(new Photo(c, f.getName().toString().replace(".jpg", ""), f.getAbsolutePath()));
		}
	}

}
