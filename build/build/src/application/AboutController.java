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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Controls the about window
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class AboutController {

	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	@FXML private static ImageView logo;
	@FXML private static Label titleLbl;
	@FXML private static Label createdLbl;
	@FXML private static Hyperlink link;
	@FXML private static Label versionLbl;
	
	/**
	 * Sets the sizes of the components on the GUI and runs all methods needed before startup
	 */
	public static void setup(){
	     
	     Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	try {
		        		
		        		logo.setScaleX(0.06 * rem);
		        		logo.setScaleY(0.06 * rem);
		        		logo.setLayoutX(0.85 * rem);
		        		logo.setLayoutY(1.55 * rem);
		        		        		
		        		titleLbl.setPrefSize(13.2 * rem, 1.34 * rem);
		        		titleLbl.setLayoutX(13.45 * rem);
		        		titleLbl.setLayoutY(1.9 * rem);
		        		titleLbl.setFont(new Font("Segoe UI", 1.3 * rem));
		        		
		        		createdLbl.setPrefSize(13.2 * rem, 1.34 * rem);
		        		createdLbl.setLayoutX(13.45 * rem);
		        		createdLbl.setLayoutY(3.5 * rem);
		        		createdLbl.setFont(Methods.lblFont);
		        		
		        		link.setPrefSize(13.2 * rem, 1.34 * rem);
		        		link.setLayoutX(13.45 * rem);
		        		link.setLayoutY(4.5 * rem);
		        		link.setFont(Methods.lblFont);
		        		
		        		versionLbl.setPrefSize(13.2 * rem, 1.34 * rem);
		        		versionLbl.setLayoutX(13.45 * rem);
		        		versionLbl.setLayoutY(5.5 * rem);
		        		versionLbl.setFont(Methods.lblFont);
		        		versionLbl.setText(MainController.getVersionStr());
		        		
					} catch (Exception e) {
						e.printStackTrace();
					}
		        	

		        }
		   });
	
	}
	
	/**
	 * Open the link to shayConcepts
	 */
	public static void openLink(){
		if (Desktop.isDesktopSupported()) {
		      try {
		    	URI uri = new URI("http://shayConcepts.com");
		        Desktop.getDesktop().browse(uri);
		      } catch (IOException e) { 
		    	  Methods.loadPopup("Cannot open link", PopupController.ERROR, About.getPopX(), About.getPopY());
		      } catch (URISyntaxException e) {
		    	  Methods.loadPopup("Bad link syntax", PopupController.ERROR, About.getPopX(), About.getPopY());
			}
	    } else { 
	    	Methods.loadPopup("Java does not support your OS", PopupController.ERROR, About.getPopX(), About.getPopY());
	    }
	}

}
