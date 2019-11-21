/*
Author: Manohar Chitoda
Data: ${date}
 */
package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import app.Album;
import app.Photo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SlideshowController 
{
	@FXML Button right, left;
	@FXML ImageView currentImage;
	int j = 0;
	private double orgReleaseSceneX;
	private double orgCliskSceneX;
	private List<Photo> photos;
	private Image images[];
	public SlideshowController()

	{
		// TODO Auto-generated constructor stub
	}

	public void init(Album album) throws FileNotFoundException
	{
		photos = album.getAllPhotosList();
		images= new Image[photos.size()];

		for (int i = 0; i < photos.size(); i++)
		{
			FileInputStream fis = new FileInputStream(photos.get(i).getPath());
			images[i] = new Image(fis);
		}

		currentImage.setImage(images[j]);
		currentImage.setCursor(Cursor.CLOSED_HAND);
		currentImage.setOnMousePressed(circleOnMousePressedEventHandler);

		currentImage.setOnMouseReleased(e -> {
			orgReleaseSceneX = e.getSceneX();
			if (orgCliskSceneX > orgReleaseSceneX) {
				left.fire();
			} else {
				right.fire();
			}
		});
	}

	EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent t)
		{
			orgCliskSceneX = t.getSceneX();
		}
	};

	public void onClick(ActionEvent event) 
	{
		Button b = (Button)event.getSource();
		if(b.equals(right))
		{
			j = j + 1;
			if (j == photos.size()) {
				j = 0;
			}
			currentImage.setImage(images[j]);
		}
		else 
			if(b.equals(left))
			{
				j = j - 1;
				if (j == 0 || j > photos.size() + 1 || j == -1)
				{
					j = photos.size() - 1;
				}
				currentImage.setImage(images[j]);
			}
	}
}
