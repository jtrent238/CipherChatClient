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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controls the help window
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class HelpController {

	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	@FXML private static TextArea helpBox;
	
	private static Stage primaryStage;
	
	/**
	 * Sets the sizes of the components on the GUI and runs all methods needed before startup
	 */
	public static void setup(){
	     
		helpBox.setText(
			"-FAQs-\n\n"+
			"How does Cipher Chat work?\n"+
			"Cipher Chat is chat room software. It's not like Skype where you create a "+
			"username and have a list of friends. You connect to a chat room and talk "+ 
			"with people currently in that same chat room. All messages and usernames "+
			"are encrypted to prevent anyone from snooping on the conversation."+
			
			"\n\nWhat is an IP address?\n"+
			"An IP address is like the street address for a computer. It's how "+		
			"computers know where to send data."+
			
			"\n\nWhere do I get this IP address?\n"+
			"The person who is running the server program needs to provide their IP address."+
			
			"\n\nWhat is a Port?\n"+
			"A port on a network allows traffic to be directed to the correct computer and application on the network. "+
			"The IP address tells the client program to "+	
			"go to the correct network, and the port then directs them to the server program."+
			
			"\n\nWhere do I get this Port?\n"+
			"The person who is running the server program needs to provide the correct port."+
			
			"\n\nWhat happens when I am muted?\n"+
			"You cannot send messages in the chatroom until the server administrator has unmuted you."+
			
			"\n\nGetting errors that the program is trying to write files?\n"+
			"Make sure to run the program as adminstrator. Right click on the program and click \"Run as administrator\""
			
			);
		
	     Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	try {        		
		        		helpBox.setPrefSize(25.8 * rem, 17.2 * rem);
		        		helpBox.setLayoutX(0.85 * rem);
		        		helpBox.setLayoutY(1.8 * rem);
		        		
					} catch (Exception e) {
						e.printStackTrace();
					}
		        	

		        }
		   });
	     
	     setPrimaryStage(Help.getPrimaryStage2());
	     
	}

	
	/**
	 * Gets the primary stage of the window so that the window can be closed 
	 * @return primaryStage - Stage
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Sets the primaryStage so that the window can be closed
	 * @param primaryStage - Stage
	 */
	public static void setPrimaryStage(Stage primaryStage) {
		HelpController.primaryStage = primaryStage;
	}



}
