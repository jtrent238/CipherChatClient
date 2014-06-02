/*
Copyright (C) 2014 Andrew Shay shayConcepts

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software") for free and 
non commercial purposes, to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, 
sublicense, copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright 
notice and this permission notice shall be included in all copies and substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR 
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package application;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Opens the Popup window
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class PopupMain extends Application{
	
	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	public void start(Stage primaryStage, double parentX, double parentY) throws Exception {
		try {
			Group root = new Group(); //Setup a group to hold items for the stage
            AnchorPane page = (AnchorPane) FXMLLoader.load(application.PopupMain.class.getResource("Popup.fxml"));
            page.setPrefWidth(PopupController.getWidth());
            page.setPrefHeight(7 * rem);
            
            //Set location of window
            primaryStage.setX(parentX - (page.getPrefWidth() / 2));
            primaryStage.setY(parentY - (page.getPrefHeight() / 2));
            
            root.getChildren().add(page);

            WindowBorder borderPane = new WindowBorder("", (int)page.getPrefWidth(), primaryStage);
            root.getChildren().add(borderPane);      
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Popup");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.getIcons().add(new Image("/images/icon.png"));
            
            primaryStage.show();         
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
	}

}
