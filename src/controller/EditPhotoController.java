package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import app.Album;
import app.Photo;
import app.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author manoharchitoda
 * @author surajupadhyay
*/
public class EditPhotoController 
{
	@FXML Button done, addTag, deleteTag, cancel, editTag;
	@FXML TextArea caption;
	@FXML ImageView imageV;
	@FXML ListView<String> tagList;
	ObservableList<String> tagObs;
	private Photo photo;
	private Album album;
	
	/**
	 * @param album ~ current album
	 * @param photo ~ current selected photo
	 * @throws FileNotFoundException the FIS may not find the file
	 */
	public void init(Album album, Photo photo) throws FileNotFoundException 
	{
		this.album = album;
		this.photo = photo;
		FileInputStream fis = new FileInputStream(this.photo.getPath());
		
		this.imageV.setImage(new Image(fis));
		this.caption.setText(this.photo.getCaption());
		tagObs = FXCollections.observableArrayList();
		Map<String, Tag> tags = this.photo.getTags();
		for(String key : tags.keySet())
		{
			String tagName = tags.get(key).getTagName();
			String tagValue = tags.get(key).getTagValue();
			tagObs.add(tagName + ":" + tagValue);
		}
		this.tagList.setItems(tagObs);
		tagList.getSelectionModel().selectFirst();
	}
	
	/**
	 * @param event on Clicking action
	 */
	public void onClicked(ActionEvent event)
	{
		Button b = (Button)event.getSource();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		if(b.equals(addTag)) 
		{
			 String [] s = input(stage, "", "").split("%");
			 if(s.length == 2)
			 {
				 photo.addTag(s[0], s[1]);
				 tagObs.add(s[0]+":"+s[1]);
				 tagList.setItems(tagObs);
			 }
			 else
				 alert(stage);
		}
		
		else if(b.equals(deleteTag))
		{
			String s = tagList.getSelectionModel().getSelectedItem();
			String t = s.replaceFirst(":", "");
			if(photo.getTags().containsKey(t))
			{
				photo.getTags().remove(t);
				tagObs.remove(tagList.getSelectionModel().getSelectedIndex());
				tagList.setItems(tagObs);
			}
		}
		
		else if(b.equals(editTag))
		{
			String str = tagList.getSelectionModel().getSelectedItem();
			String key = str.replaceFirst(":", "");
			
			
			if(photo.getTags().containsKey(key))
			{
				String [] arr = str.split(":");
				String tagN = "-";
				String tagV = "-";
				if(arr[0].length() > 0) tagN = arr[0];
				if(arr.length > 1) 
				{
					tagV = arr[1];
				}
				
				arr = input(stage, tagN, tagV).split("%");
				tagN = "-";
				tagV = "-";
				if(arr[0].length() > 0) tagN = arr[0];
				if(arr.length > 1) 
				{
					tagV = arr[1];
				}
				
				photo.getTags().remove(key);
				photo.addTag(tagN, tagV);
				
				tagObs.remove(str);
				tagObs.add(tagN+":"+tagV);
				tagList.setItems(tagObs);
				
			}
		}
	}
	
	/**
	 * @param event ~ an action event
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void commit(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		loader.setLocation(getClass().getResource("/view/UserPhotos.fxml"));

		if(event.getSource().equals(done))
		{
			if(this.caption.getText() != null)
			{
				this.photo.setCaption(this.caption.getText());
			}
			else
			{
				alert(stage);
			}
		}
		BorderPane root = (BorderPane)loader.load();
		UserPhotosController upc = loader.getController();
		upc.init(album, stage);;
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}

	/**
	 * @param mainStage the primary stage
	 * @param name the name of the tag
	 * @param value the value of the tag name
	 * @return String the input from the user
	 */
	private String input(Stage mainStage, String name, String value)
	{
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Photo Tag");
		dialog.setHeaderText("Please enter the tag values.");
		dialog.setResizable(false);
		 
		Label label1 = new Label("Tag title: ");
		Label label2 = new Label("Tag value: ");
		TextField text1 = new TextField(name);
		TextField text2 = new TextField(value);
		         
		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(text1, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(text2, 2, 2);
		dialog.getDialogPane().setContent(grid);
		         
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		 
		dialog.setResultConverter(new Callback<ButtonType, String>() 
		{
		    @Override
		    public String call(ButtonType b) 
		    {
		 
		        if (b == buttonTypeOk) 
		        {
		            return (text1.getText() + "%" +text2.getText());
		        }
		        return name + "%" + value;
		    }
		});
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
		     return result.toString().substring(9, result.toString().length()-1);
		}
		return name + "%" + value;
	}
	
	/**
	 * @param mainstage ~ current stage
	 * @return boolean ~ what the user chooses
	 */
	private boolean alert(Stage mainstage)
	{
		boolean choice = false;
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainstage);
		alert.setTitle("Bad Input");
		String content = "Please enter valid input!";
		alert.setContentText(content);
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK)
			choice = true;
		return choice;
	}
}
