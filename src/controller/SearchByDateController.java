/*
Author: Manohar Chitoda
Data: ${date}
*/
package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import app.Album;
import app.Photo;
import app.Search;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SearchByDateController 
{
	@FXML Button cancel, search;
	@FXML DatePicker start, end;


	/**
	 * 
	 * @param event click action
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClick(ActionEvent event) throws IOException
	{
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		BorderPane root = null;
		
		if(b.equals(cancel))
		{
			loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
			root = (BorderPane)loader.load();
			
			UserHomeController ush = loader.getController();			
			ush.init(stage);
		}
		//search button pressed
		else
		{
			LocalDate s = start.getValue();
			LocalDate e = end.getValue();
			Calendar st = Calendar.getInstance();
			Calendar ed = Calendar.getInstance();
			st.set(s.getYear(),s.getMonthValue()-1 , s.getDayOfMonth());
			ed.set(e.getYear(),e.getMonthValue()-1 , e.getDayOfMonth());
			
			List<Photo> searchedPhotos = Search.searchByDateRange(st, ed);
			Album searchedPhotosAlbum = new Album("Search Results", searchedPhotos);
			loader.setLocation(getClass().getResource("/view/SearchResults.fxml"));
			root = (BorderPane)loader.load();
			((SearchResultsController)loader.getController()).init(searchedPhotosAlbum);
			stage.setTitle(stage.getTitle() + "/" + "Search Results");
		}
		
		Scene newScene= new Scene(root);
		stage.setScene(newScene);
		stage.show();
	}
	
}
