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

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controls the update window
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class UpdateController {

	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	@FXML private static TextArea updateBox;
	@FXML private static Button download;
	@FXML private static Button cancel;
	@FXML private static ProgressBar progress;
	
	private static Stage primaryStage;
	private static String updateLink = null;
	private static String changelog = "Failed to load changelog - 1";
	
	/**
	 * Sets the sizes of the components on the GUI and runs all methods needed before startup
	 */
	public static void setup(){
	     
	     Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	try {
		        		
		        		
		        		updateBox.setPrefSize(25.8 * rem, 13.35 * rem);
		        		updateBox.setLayoutX(0.85 * rem);
		        		updateBox.setLayoutY(1.8 * rem);
		        		
		        		download.setPrefSize(5 * rem, 1 * rem);
		        		download.setLayoutX(15.5 * rem);
		        		download.setLayoutY(17.85 * rem);
		        		
		        		cancel.setPrefSize(5 * rem, 1 * rem);
		        		cancel.setLayoutX(21.7 * rem);
		        		cancel.setLayoutY(17.85 * rem);
		        		
		        		progress.setPrefSize(25.9 * rem, 1.2 * rem);
		        		progress.setLayoutX(0.85 * rem);
		        		progress.setLayoutY(16 * rem);
		        		
					} catch (Exception e) {
						e.printStackTrace();
					}
		        	

		        }
		   });
	     
	     setPrimaryStage(Update.getPrimaryStage2());
	     loadData();
	     
	}
	
	/**
	 * Runs when the download button is clicked
	 */
	public static void downloadClicked(){
		
		Runnable rCheck = new Runnable(){
			public void run(){
				try{
					DownloadUpdate.runUpdate(Methods.getProgramPath(), getUpdateLink());
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		Thread tCheck = new Thread(rCheck);
		tCheck.start();
		
	}
	
	/**
	 * Runs when the cancel button is clicked
	 */
	public static void cancelClicked(){
		try {
			DownloadUpdate.fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	getPrimaryStage().close();
		        }
		    });
		}	
	}
	
	/**
	 * Returns the link to the new installer
	 * @return updateLink - String
	 */
	public static String getUpdateLink() {
		return updateLink;
	}
	
	/**
	 * Sets the link for the new installer
	 * @param updateLink - String
	 */
	public static void setUpdateLink(String updateLink) {
		UpdateController.updateLink = updateLink;
	}
	
	/**
	 * Loads the main title and the changelog into the update window
	 */
	public static void loadData(){
		
		getChangeLog();
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	updateBox.setText(getChangelog());
	        }
	     });
	}
	
	/**
	 * Downloads the changelog
	 */
	public static void getChangeLog(){
		//Check for changelog
		try{
			URL link = new URL("http://shayconcepts.com/programming/ChatClient/changelog.txt");
			Scanner sc = new Scanner(link.openStream()); //Takes the downloaded HTML page and sends it to a scanner
					
			//This section converts the html page to the string
			String contents = "";
			while(sc.hasNextLine() || sc.hasNext()){
				contents = contents + sc.nextLine() + "\n"; //Contents is the html page as a string
			}
			sc.close();//Closes the scanner file
			setChangelog(contents);
		}
		catch(Exception e){
			Methods.loadPopup("Could not download changelog", PopupController.ERROR, Update.getPopX(), Update.getPopY());
		}
		finally{
			
		}

		
	}

	/**
	 * Returns the text of the changelog
	 * @return changelog - String
	 */
	public static String getChangelog() {
		return changelog;
	}

	/**
	 * Sets the text of the changelog
	 * @param changelog - String
	 */
	public static void setChangelog(String changelog) {
		UpdateController.changelog = changelog;
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
		UpdateController.primaryStage = primaryStage;
	}
	
	/**
	 * Updates the progress of the progress bar
	 * @param progress
	 */
	public static void updateProgress(final double progress){
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	UpdateController.progress.setProgress((double)progress/100);        	
	        }
	     });
	}


}
