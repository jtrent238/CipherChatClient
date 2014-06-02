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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Misc. methods used by multiple classes
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class Methods {
	
	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	public static final Font lblFont = new Font("Segoe UI", 0.74 * rem);
	
	/**
	 * Returns the main program path of the main jar file
	 * @return parentPath + File.separator
	 */
	public static String getProgramPath(){
		URL url = Main.class.getProtectionDomain().getCodeSource().getLocation(); //Gets the path
		String jarPath = null;
		try {
			jarPath = URLDecoder.decode(url.getFile(), "UTF-8"); //Should fix it to be read correctly by the system
		} catch (UnsupportedEncodingException e) {
			Methods.loadPopup("Failed decoding path", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			e.printStackTrace();
		}
		String parentPath = new File(jarPath).getParentFile().getPath(); //Path of the jar
		return parentPath + File.separator;
	}
	
	/**
	 * Displays a popup menu
	 * @param message - The text of the popup
	 * @param type - The type of error, ERROR, WARNING, INFO
	 * @param parentX - X location of parent window
	 * @param parentY - Y location of parent window
	 */
	public static void loadPopup(final String message, final int type, final double parentX, final double parentY){
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	try {
	        		PopupController.set(message, type);
					new PopupMain().start(new Stage(), parentX, parentY);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	   });
	}
	
}
