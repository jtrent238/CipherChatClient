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
	
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Opens the chat room window
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class ChatRoom extends Application {
	
	private static Stage primaryStage;
	
	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	public void start(Stage primaryStage, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
		
		setPrimaryStage(primaryStage);
		
		try {
			
			Group root = new Group(); //Contains items on the stage
			AnchorPane page = (AnchorPane) FXMLLoader.load(application.ChatRoom.class.getResource("ChatRoom.fxml")); //Loads FXML
			page.setPrefSize(36.7 * rem, 19 * rem);
			
			//Set location of window
            primaryStage.setX(Main.getPopX() - (page.getPrefWidth() / 2));
            primaryStage.setY(Main.getPopY() - (page.getPrefHeight() / 2));
			
			root.getChildren().add(page);
			
			WindowBorderChatroom borderPane = new WindowBorderChatroom("", (int)page.getPrefWidth(), primaryStage);
			root.getChildren().add(borderPane);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(ChatRoom.class.getResource("/application/style.css").toExternalForm());
			
			ChatRoomController.setup(socket, oos, ois); //Sets the sizes of the components on the GUI
			
			primaryStage.setScene(scene);
			primaryStage.setTitle(MainController.getChatTitle());
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.getIcons().add(new Image("images/icon.png"));
			
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage){
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Returns the primary stage
	 * @return primaryStage
	 */
	protected static Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Sets the primary stage
	 * @param primaryStage
	 */
	private static void setPrimaryStage(Stage primaryStage) {
		ChatRoom.primaryStage = primaryStage;
	}
	
	/**
	 * Returns the X coor needed for the popup eg errors
	 * @return double
	 */
	public static double getPopX(){
		return getPrimaryStage().getX() + (getPrimaryStage().getWidth() / 2);
	}
	
	/**
	 * Returns the Y coor needed for the popup eg. errors
	 * @return double
	 */
	public static double getPopY(){
		return getPrimaryStage().getY() + (getPrimaryStage().getHeight() / 2);
	}
}
