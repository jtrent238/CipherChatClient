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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Creates a popup window
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class PopupController {
	
	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	@FXML private static Label messageLabel;
	@FXML private static ImageView image;
	@FXML private static AnchorPane anchorOne;
	
	public static final int ERROR = 0;
	public static final int WARNING = 1;
	public static final int INFO = 2;
	public static final int BLANK = 3;
	
	private static String message;
	private static int width;
	private static String fontName;
	
	/**
	 * Sets the message and the type of message
	 * @param message - String
	 * @param type - int
	 */
	public static void set(String message, int type){
		setGUI();
		setMessage(message);
		setType(type);	
	}
	
	/**
	 * Resets GUI components to work on all resolutions
	 */
	private static void setGUI(){
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	try {
	        		messageLabel.setFont(Methods.lblFont);
	        		messageLabel.setLayoutX(7 * rem);
	        		messageLabel.setLayoutY(3.5 * rem);
	        		
	        		image.setScaleX(.064 * rem);
	        		image.setScaleY(.064 * rem);
	        		image.setLayoutX(1 * rem);
	        		image.setLayoutY(1.5 * rem);
	       	     
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	   });
	}
	
	/**
	 * Sets the width of the window
	 * @param message - String
	 */
	private static void setWidth(String message){
		Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB).getGraphics();
		fontName = g.getFont().getName();
		g.dispose();
		
		Font font = new Font(fontName, Font.PLAIN, (int) (0.74 * rem));
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		FontMetrics fm = img.getGraphics().getFontMetrics(font);
		PopupController.width = fm.stringWidth(message);
		//PopupController.width = (int) Math.rint(new Text(message).getLayoutBounds().getHeight());
		
		width += 10 * rem;
	}
	
	/**
	 * Sets the messages of the window
	 * @param message - String
	 */
	private static void setMessage(String message) {
		PopupController.message = message;
		setWidth(message);
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	messageLabel.setText(PopupController.message);
	        }
	   });
	}

	/**
	 * Sets the type of the message
	 * @param type - int
	 */
	private static void setType(int type) {
		if(type == ERROR){
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	image.setImage(new Image("/images/error.png"));
		        }
		    });
			
		}
		else if(type == INFO){
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	image.setImage(new Image("/images/info.png"));
		        }
		    });
			
		}
		else if(type == WARNING){
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	image.setImage(new Image("/images/warning.png"));
		        }
		    });
		}
		else if(type == BLANK){
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	image.setImage(new Image("/images/blank.png"));
		        }
		    });
		}
		
	}

	/**
	 * Returns the width of the window
	 * @return width - int
	 */
	public static int getWidth() {
		return width;
	}

	/**
	 * Sets the width var to be used when sizing the window
	 * @param width - int
	 */
	public static void setWidth(int width) {
		PopupController.width = width;
	}
	
}
