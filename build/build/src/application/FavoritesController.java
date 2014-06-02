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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * Controls the favorites window
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class FavoritesController {

	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	@FXML private static TextField serverIPBox;
	@FXML private static Label serverIPLbl;
	@FXML private static Label portLbl;
	@FXML private static TextField portBox;
	@FXML private static Label passwordLbl;
	@FXML private static PasswordField passwordBox;
	@FXML private static Label usernameLbl;
	@FXML private static TextField usernameBox;
	@FXML private static Button addButton;
	@FXML private static Button connectButton;
	@FXML private static TableView<Favorite> favoriteTable = new TableView<Favorite>();
	private static ObservableList<Favorite> favoriteList = FXCollections.observableArrayList(); //List for clients, only includes the fully connected clients

	/**
	 * Setups up GUI elements
	 */
	protected static void setup(){
		
		Platform.runLater(new Runnable() {
	        @SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
	        public void run() {
	        	try {
	        		
	        		favoriteTable.setPrefSize(23.05 * rem, 18 * rem);
	        		favoriteTable.setLayoutX(0.85 * rem);
	        		favoriteTable.setLayoutY(1.8 * rem);
	        		
	        		serverIPLbl.setPrefSize(10.8 * rem, 1.34 * rem);
	        		serverIPLbl.setLayoutX(0.85 * rem);
	        		serverIPLbl.setLayoutY(20 * rem);
	        		serverIPLbl.setFont(Methods.lblFont);
	        		
	        		serverIPBox.setPrefSize(10.8 * rem, 1.34 * rem);
	        		serverIPBox.setLayoutX(0.85 * rem);
	        		serverIPBox.setLayoutY(21.5 * rem);
	        		
	        		portLbl.setPrefSize(10.8 * rem, 1.34 * rem);
	        		portLbl.setLayoutX(13.2 * rem);
	        		portLbl.setLayoutY(20 * rem);
	        		portLbl.setFont(Methods.lblFont);
	        		
	        		portBox.setPrefSize(10.8 * rem, 1.34 * rem);
	        		portBox.setLayoutX(13.2 * rem);
	        		portBox.setLayoutY(21.5 * rem);
	        		
	        		passwordLbl.setPrefSize(10.8 * rem, 1.34 * rem);
	        		passwordLbl.setLayoutX(0.85 * rem);
	        		passwordLbl.setLayoutY(23.3 * rem);
	        		passwordLbl.setFont(Methods.lblFont);
	        		
	        		passwordBox.setPrefSize(10.8 * rem, 1.34 * rem);
	        		passwordBox.setLayoutX(0.85 * rem);
	        		passwordBox.setLayoutY(24.8 * rem);
	        		
	        		usernameLbl.setPrefSize(10.8 * rem, 1.34 * rem);
	        		usernameLbl.setLayoutX(13.2 * rem);
	        		usernameLbl.setLayoutY(23.3 * rem);
	        		usernameLbl.setFont(Methods.lblFont);
	        		
	        		usernameBox.setPrefSize(10.8 * rem, 1.34 * rem);
	        		usernameBox.setLayoutX(13.2 * rem);
	        		usernameBox.setLayoutY(24.8 * rem);
	        		
	        		addButton.setPrefSize(23.15 * rem, 1.34 * rem);
	        		addButton.setLayoutX(0.85 * rem);
	        		addButton.setLayoutY(26.8 * rem);
	        		
	        		connectButton.setPrefSize(23.15 * rem, 1.34 * rem);
	        		connectButton.setLayoutX(0.85 * rem);
	        		connectButton.setLayoutY(28.7 * rem);
	        		
	        		favoriteTable.setEditable(true);
	        		favoriteTable.getColumns().clear(); //Remove the column from SceneBuilder
	        		
	        		TableColumn ipCol = new TableColumn("Server IP");
	        		ipCol.setPrefWidth(6.4 * rem);
	        		ipCol.setCellValueFactory(new PropertyValueFactory<Favorite, String>("IP"));
	        		
	        		TableColumn portCol = new TableColumn("Port");
	        		portCol.setPrefWidth(3.5 * rem);
	        		portCol.setCellValueFactory(new PropertyValueFactory<Favorite, String>("port"));
	        		
	        		
	        		TableColumn passwordCol = new TableColumn("Server Password");
	        		passwordCol.setPrefWidth(7 * rem);
	        		passwordCol.setCellValueFactory(new PropertyValueFactory<Favorite, String>("passSet"));
	        		
	        		
	        		TableColumn usernameCol = new TableColumn("Username");
	        		usernameCol.setPrefWidth(6 * rem);
	        		usernameCol.setCellValueFactory(new PropertyValueFactory<Favorite, String>("username"));
	        		
	        		favoriteTable.setItems(favoriteList);
	        		favoriteTable.getColumns().addAll(ipCol, portCol, passwordCol, usernameCol);
	        		
	        		
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	   });
		
		favoriteList.clear();
		readFavoriteList();
	
	}
	

	/**
	 * Runs when Connect button is clicked
	 */
	public static void connectClicked(){
		MainController.favConnect(favoriteTable.getSelectionModel().getSelectedItem());
		Favorites.getPrimaryStage2().close();
	}
	
	/**
	 * Runs when the Add button is clicked
	 */
	public static void addClicked(){
		addFav(serverIPBox.getText() + "," + portBox.getText() + "," + passwordBox.getText() + "," + usernameBox.getText());
		serverIPBox.clear();
		portBox.clear();
		passwordBox.clear();
		usernameBox.clear();
		writeFavoriteList();
	}
	
	/**
	 * Adds a favorite
	 * @param line
	 */
	private static void addFav(String line){
		String[] split = line.split(",");
		Favorite fav = new Favorite(split[0], split[1], split[2], split[3]);
		favoriteList.add(fav);
	}
	
	/**
	 * Writes the banned file
	 */
	private static void writeFavoriteList(){
		File favorite = createFavoriteList();
		FileWriter fstream = null;
		
		try {
			fstream = new FileWriter(favorite);
		} catch (IOException e) {
			Methods.loadPopup("Failed to prep favorite list for reading", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			e.printStackTrace();
		}
		BufferedWriter out = new BufferedWriter(fstream);
		
		for(int i = 0; i < favoriteList.size(); i++){
			try {
				Favorite fav = favoriteList.get(i);
				out.write(fav.getIP() + "," + fav.getPort() + "," + fav.getPassword() + "," + fav.getUsername());
				out.newLine();
			} catch (IOException e) {
				Methods.loadPopup("Failed to prep favorites list for reading", PopupController.ERROR, Main.getPopX(), Main.getPopY());
				e.printStackTrace();
			}
		}
		
		try {
			out.close();
			fstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the ban list file
	 */
	private static File createFavoriteList(){
		//Check to see if the file exists
		File favorite = new File(Methods.getProgramPath() + "favorites.txt");
		if(!favorite.exists()){
			try {
				favorite.createNewFile();
			} catch (IOException e) {
			     Methods.loadPopup("Could not create favoritres file", PopupController.ERROR, Main.getPopX(), Main.getPopY());
				e.printStackTrace();
			}
		}
		
		return favorite;
	}
	
	/**
	 * Reads in the list of banned IP address
	 */
	private static void readFavoriteList(){
		File favorite = createFavoriteList();
		
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(favorite);
		} catch (FileNotFoundException e) {
		     Methods.loadPopup("Error reading favoritres - fstream", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			e.printStackTrace();
		}
		
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			while((line = br.readLine()) != null){
				FavoritesController.addFav(line);
			}
		} catch (Exception e) {
		     Methods.loadPopup("Error reading favorite line", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			e.printStackTrace();
		}
		
		try{
			br.close();
			in.close();
			fstream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		    Methods.loadPopup("Error closing streams", PopupController.ERROR, Main.getPopX(), Main.getPopY());
		}
				
	}
	
	/**
	 * Displays the context menu for the table of connected users
	 */
	public static void tableClicked(){
		
		final ContextMenu contextMenu = new ContextMenu();
		
		MenuItem item1 = new MenuItem("Connect");
		item1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	connectClicked();		    	
		    }
		});
		
		MenuItem item2 = new MenuItem("Remove");
		item2.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	int remove = favoriteTable.getSelectionModel().getSelectedIndex();
		    	favoriteList.remove(remove);
		    	writeFavoriteList();
		    }
		});
		
		MenuItem item3 = new MenuItem("Copy Server IP");
		item3.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	StringSelection stringSelection = new StringSelection(favoriteTable.getSelectionModel().getSelectedItem().getIP());
		    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    	clipboard.setContents(stringSelection, null);
		    }
		});
		
		MenuItem item4 = new MenuItem("Copy Port");
		item4.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	StringSelection stringSelection = new StringSelection(favoriteTable.getSelectionModel().getSelectedItem().getPort());
		    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    	clipboard.setContents(stringSelection, null);
		    }
		});
		
		MenuItem item6 = new MenuItem("Copy Username");
		item6.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	StringSelection stringSelection = new StringSelection(favoriteTable.getSelectionModel().getSelectedItem().getUsername());
		    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    	clipboard.setContents(stringSelection, null);
		    }
		});
		
		contextMenu.getItems().addAll(item1, item2, item3, item4, item6);
		
		//Popup menu for Connected Users table
		favoriteTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent t) {
	            if(t.getButton() == MouseButton.SECONDARY)
	            {
	            	contextMenu.show(favoriteTable, t.getScreenX() , t.getScreenY());
	            }
	        }
	    });
	}
	
}
