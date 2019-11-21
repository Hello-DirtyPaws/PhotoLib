package controller;

import java.io.IOException;

import app.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AllUsers;

/**
 * @author manoharchitoda
 * @author surajupadhyay
 *
 */
public class LoginController
{
	@FXML Button login;
	@FXML TextField username;
	@FXML Label error;
	
	
	/**
	 * @param e which ever button is clicked
	 * @throws IOException the loader might not load the FXML as requested
	 */
	public void onClick(ActionEvent e) throws IOException 
	{
		FXMLLoader loader = new FXMLLoader();
		String input = username.getText();
		Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		if(input != null)
		{
			if(input.equalsIgnoreCase("admin"))
			{
				stage.setTitle("Photos/Admin");
				loader.setLocation(getClass().getResource("/view/AdminHome.fxml"));
				BorderPane root = (BorderPane)loader.load();
				Scene newScene= new Scene(root);
				stage.setScene(newScene);
				stage.show();
			}
			else if(input.equalsIgnoreCase("stock"))
			{
				loader.setLocation(getClass().getResource("/view/StockHome.fxml"));
				BorderPane root = (BorderPane)loader.load();
				StockHomeController shc = loader.getController();
				shc.init(stage);
				Scene newScene= new Scene(root);
				stage.setScene(newScene);
				stage.show();
			}
			else
			{
				User user = AllUsers.getAllUsersObj().getUserByName(input);
				if(user == null)
				{	
					this.error.setTextFill(Color.web("#800000"));
					this.error.setText("*User does not exist, try again!");
				}
				
				else
				{
					this.error.setTextFill(Color.web("#008000"));
					this.error.setText("Welcome! " + user.getUsername());
					
					AllUsers.setCurrUser(input.trim());
					loader.setLocation(getClass().getResource("/view/UserHome.fxml"));
					BorderPane root = (BorderPane)loader.load();
					UserHomeController uhc = loader.getController();
					uhc.init(stage);
					Scene newScene= new Scene(root);
					stage.setScene(newScene);
					stage.show();
				}
			}
		}	
	}
		
}
