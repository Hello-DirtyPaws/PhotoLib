/**
 * @author manoharchitoda
 * @author surajupadhyay
*/
package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import app.Album;
import app.Photo;
import app.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DisplayPhotoController 
{
	@FXML Button close;
	@FXML Label caption, lc, person, photo, time, date;
	@FXML ListView<String> tagList;
	@FXML ImageView image;
	ObservableList<String> tagsObs;
	Album album;
	
	/**
	 * @param e which ever button is clicked
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClicked(ActionEvent e) throws IOException
	{
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		stage.close();
	}
	
	/**
	 * @param photo ~ current selected photo
	 * @param stage ~ separate stage
	 * @param album ~ current album
	 * @throws FileNotFoundException ~ the FIS may not find the file
	 */
	public void init(Photo photo, Stage stage, Album album) throws FileNotFoundException
	{
		FileInputStream fis = new FileInputStream(photo.getPath());
		this.album = album;
		
    	this.image.setImage(new Image(fis));
		this.caption.setText(photo.getCaption());
		this.time.setText(photo.getHour() + ":" + photo.getMin());
		this.date.setText(photo.getDay() + "/" + photo.getMonth()+"/"+photo.getYear());
		
		tagsObs = FXCollections.observableArrayList();
		Map<String, Tag> tags = photo.getTags();
		for(String key : tags.keySet())
		{
			String tagName = tags.get(key).getTagName();
			String tagValue = tags.get(key).getTagValue();
			tagsObs.add(tagName + ":" + tagValue);
		}
		
		this.tagList.setItems(tagsObs);
	}
	
}