
package app;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AllUsers;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Photos extends Application 
{
	public void start(Stage primaryStage) throws IOException 
	{
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() 
		{
			@Override
			public void handle(WindowEvent e) 
			{
				try 
				{
					AllUsers.writeApp(AllUsers.getAllUsersObj());
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		
		primaryStage.setTitle("Photos");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		BorderPane root = (BorderPane)loader.load();
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) 
	{
		//Assigning the copying data to null in every start
		User.setCopiedAlbum(null);
		Album.setCopiedPhoto(null);
		Album.setCopiedPhotoFrom(null);
		launch(args);
	}
}
