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

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Creates the custom window border with window buttons
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class WindowBorderChatroom extends BorderPane{
	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	private double borderHeight = 1.3 * rem; //The height of your border where the title and title buttons are
	boolean closeApp = false; //If TRUE, the whole program will close 

	/**
	 * Constructor for the window border - closing will not exit the program
	 * @param title - Title of window
	 * @param width - Width of window
	 * @param primaryStage - Used to move window via top bar
	 */
	public WindowBorderChatroom(String title, int width, Stage primaryStage) {
		this.setStyle("-fx-background-color: transparent;");//No background color
        this.setCenter(new WindowTitleBox(title, width)); //Title of Window
        this.setTop(new WindowToolbar(width, primaryStage));//The toolbar to move the window    
        this.setRight(new WindowButtons(primaryStage));//The Window buttons
	}
	
	/**
	 * Constructor for the window border - closing will exit the whole Java program
	 * @param title - Title of window
	 * @param width - Width of window
	 * @param primaryStage - Used to move window via top bar
	 */
	public WindowBorderChatroom(String title, int width, Stage primaryStage, boolean close) {
		this.setStyle("-fx-background-color: transparent;");//No background color
        this.setCenter(new WindowTitleBox(title, width)); //Title of Window
        this.setTop(new WindowToolbar(width, primaryStage));//The toolbar to move the window    
        this.setRight(new WindowButtons(primaryStage));//The Window buttons
        this.closeApp = close;
	}
	
	/**
	 * An outer box to hold the window title so it can be padded
	 * @author Andrew Shay - http://shayConcepts.com
	 *
	 */
	public class WindowTitleBox extends HBox {
		public WindowTitleBox(String title, int width) {
			this.setPadding(new Insets(-1.18 * rem, 0, 0, 0.8 * rem));
			this.getChildren().add(new WindowTitle(title, width));//Add the label to this HBox
		}
		
		/**
		 * The title of the window
		 * @author Andrew Shay - http://shayConcepts.com
		 *
		 */
		public class WindowTitle extends Label {
			public WindowTitle(String title, int width) {
				this.setMaxWidth(width - 63 - 20 - 10); //Width of the current window minus the width of the window buttons minus the padding from the left for the label minus an extra 10
				//this.setTextFill(Color.BLUE); //Doesn't actually work. Gets overridden by the CSS .label
				this.setStyle("-fx-text-fill: mainText;");//Text color
				this.setText(title);
			}

		}

	}
	
	/**
	 * The window buttons
	 * @author Andrew Shay - http://shayConcepts.com
	 *
	 */
	public class WindowButtons extends HBox {

		public WindowButtons(final Stage primaryStage) {
			this.setSpacing(rem / 3); //Spacing between objects in the HBox
			this.setPadding(new Insets((-1 * borderHeight), 0, 0, 0)); //Padding for the HBox
			
			//Minimize button
	        Image minimize = new Image("images/minimize.png");
	        final ImageView minimizeImg = new ImageView();
	        minimizeImg.setScaleX(0.062 * rem);
	        minimizeImg.setScaleY(0.06 * rem);
	        
	        minimizeImg.setImage(minimize);
	        minimizeImg.setOnMouseEntered(new EventHandler<MouseEvent>(){
	        	@Override
	        	public void handle(MouseEvent t){
	        		minimizeImg.setImage(new Image("images/minimize_hover.png"));
	        	}
	        });
	        minimizeImg.setOnMouseExited(new EventHandler<MouseEvent>(){
	        	@Override
	        	public void handle(MouseEvent t){
	        		minimizeImg.setImage(new Image("images/minimize.png"));
	        	}
	        });
	        minimizeImg.setOnMouseClicked(new EventHandler<MouseEvent>(){
	        	@Override
	        	public void handle(MouseEvent t){
	        		primaryStage.setIconified(true);
	        	}
	        });     
	        this.getChildren().add(minimizeImg);
	        
	        //Close button
			Image close = new Image("images/close.png");
	        final ImageView closeImg = new ImageView();
	        closeImg.setScaleX(0.062 * rem);
	        closeImg.setScaleY(0.06 * rem);
	        
	        closeImg.setImage(close);
	        closeImg.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					closeImg.setImage(new Image("images/close_hover.png"));
				}
	        });
	        
	        closeImg.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					closeImg.setImage(new Image("images/close.png"));
				}
	        });
	        
	        closeImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent arg0) {
					MainController.disconnect();
				}
	        });
	        this.getChildren().add(closeImg);
	    }
		
	}
	
	
	/**
	 * The toolbar that allows the user to move the window
	 * @author Andrew Shay - http://shayConcepts.com
	 *
	 */
	public class WindowToolbar extends ToolBar {
		
		private double initX = 0;
		private double initY = 0;
		private Stage primaryStage2;
		
		public WindowToolbar(int width, Stage primaryStage){
			primaryStage2 = primaryStage;
	        this.setStyle("-fx-background-color: transparent;");
	        this.setPrefHeight(borderHeight);
	        this.setMinHeight(borderHeight);
	        this.setMaxHeight(borderHeight);
	        this.setPrefWidth(width);
	        this.setMinWidth(width);
	        this.setPrefWidth(width);
	        
	        //when mouse button is pressed, save the initial position of screen          
	        this.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {
	                initX = me.getScreenX() - primaryStage2.getX();
	                initY = me.getScreenY() - primaryStage2.getY();
	            }
	        });
	        
	        //when screen is dragged, translate it accordingly
	        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {
	                primaryStage2.setX(me.getScreenX() - initX);
	                primaryStage2.setY(me.getScreenY() - initY);
	            }	
	        });
		}
		
	}

}
