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
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * Controls the function of the chat room
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class ChatRoomController {
	
	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	@FXML private static TextField messageBox;
	@FXML private static TextArea chatBox;
	@FXML private static ImageView sendButton;
	@FXML private static TableView<ConnectedUser> connectedUsers = new TableView<ConnectedUser>();
	private static ObservableList<ConnectedUser> clientList = FXCollections.observableArrayList(); //List of connected clients

	private static Socket socket;
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	private static Object[] fromServer;
	
	/**
	 * Sets the sizes of the components on the GUI
	 */
	public static void setup(Socket socketI, ObjectOutputStream oosI, ObjectInputStream oisI){
	     
	     Platform.runLater(new Runnable() {
		        @SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
		        public void run() {
		        	try {
		        		
		        		messageBox.setPrefSize(24.6 * rem, 1.34 * rem);
		        		messageBox.setLayoutX(0.85 * rem);
		        		messageBox.setLayoutY(16.8 * rem);
		        		
		        		chatBox.setPrefSize(26 * rem, 14 * rem);
		        		chatBox.setLayoutX(0.85 * rem);
		        		chatBox.setLayoutY(2 * rem);
		        		
		        		sendButton.setScaleX(0.06 * rem);
		        		sendButton.setScaleY(0.061 * rem);
		        		sendButton.setLayoutX(25.4 * rem);
		        		sendButton.setLayoutY(16.8 * rem);
		        		
		        		connectedUsers.setPrefSize(8.1 * rem, 16.15 * rem);
		        		connectedUsers.setLayoutX(27.7 * rem);
		        		connectedUsers.setLayoutY(2 * rem);
		        		
		        		TableColumn userCol = new TableColumn("Chatting");
		        		userCol.setPrefWidth(connectedUsers.getPrefWidth());
		        		userCol.setCellValueFactory(new PropertyValueFactory<ConnectedUser, String>("username"));
		       	     
		        		connectedUsers.setItems(clientList);
		        		connectedUsers.getColumns().addAll(userCol);
		       	     
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
		 });
	     
	     messageBox.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
				@Override public void handle(javafx.scene.input.KeyEvent e){
					if(e.getCode() == KeyCode.ENTER){
						sendMessage();
					}
				}
		});
	     
	     setSocket(socketI);
	     setOos(oosI);
	     setOis(oisI);
	     listen(); //Listen for server
	     
	}
	
	/**
	 * Listens for data from server
	 */
	private static void listen(){
		//A constant loop that listens for data from the client
		Runnable rListen = new Runnable(){

			@Override
			public void run() {
				
				//Request list of connected clients
				Object[] data = new Object[1];
				data[0] = "CLIENTS";
				sendData(data);
				
				boolean listen = true;
				while(listen){
					try{
						if((fromServer = (Object[]) ois.readObject()) != null){
							//Determine what data this is
							String tag = (String)fromServer[0]; //Getting the tag
							
							//Message to append to chat box
							if(tag.equals("Text")){
								String decryptedMessage = Crypto.decryptMessage((String)fromServer[1], (byte[])fromServer[2]);
								appendChatBox(decryptedMessage);
							}
							
							//Updates the list of connected clients
							else if(tag.equals("CLIENTS")){
								clientList.clear();		
								byte[] iv = (byte[])fromServer[1];
								
								for(int i = 2; i < fromServer.length; i++){
									String username = Crypto.decryptMessage((String)fromServer[i], iv);
									System.out.println(i + ": " + username);
									clientList.add(new ConnectedUser(username));
								}
							}
							
							else if(tag.equals("ERROR")){
								Methods.loadPopup((String)fromServer[1], PopupController.ERROR, ChatRoom.getPopX(), ChatRoom.getPopY());
							}
							
							else if(tag.equals("WARNING")){
								Methods.loadPopup((String)fromServer[1], PopupController.WARNING, ChatRoom.getPopX(), ChatRoom.getPopY());
							}
							
							else if(tag.equals("INFO")){
								Methods.loadPopup((String)fromServer[1], PopupController.INFO, ChatRoom.getPopX(), ChatRoom.getPopY());
							}
						}
					}
					catch(EOFException e){
						e.printStackTrace();
						listen = false;
						Methods.loadPopup("Connection lost", PopupController.ERROR, Main.getPopX(), Main.getPopY());
						MainController.disconnect();
					}
					catch(SocketException e){
						//FIX
						listen = false;
						
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
				}
				
				try{
					oos.flush();
					oos.close();	
					socket.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			
		};
		
		Thread tListen = new Thread(rListen);
		tListen.start();
		
	}
	
	/**
	 * Sets the socket
	 * @param socket
	 */
	private static void setSocket(Socket socket) {
		ChatRoomController.socket = socket;
	}
	
	/**
	 * Sets the output stream
	 * @param oos
	 */
	private static void setOos(ObjectOutputStream oos) {
		ChatRoomController.oos = oos;
	}
	
	/**
	 * Sets the input stream
	 * @param ois
	 */
	private static void setOis(ObjectInputStream ois) {
		ChatRoomController.ois = ois;
	}
	
	/**
	 * Sends data to the server
	 * @param data
	 */
	private static void sendData(Object[] data){
    	try {
			oos.writeObject(data);
			oos.reset();
		} catch (IOException e) {
			Methods.loadPopup("Lost connection with server", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			MainController.disconnect();
			e.printStackTrace();
		}
    }
	
	/**
	 * Sends the message from the user to the server
	 */
	public static void sendMessage(){
		byte[] iv = Crypto.getIV();
		Object[] data = new Object[3];
		data[0] = "Text";
		data[1] = Crypto.encryptMessage(messageBox.getText(), iv);
		data[2] = iv;
		
		sendData(data);
		messageBox.clear();
	}
	
	/**
	 * Appends a new message to the chat box
	 * @param message
	 */
	private static void appendChatBox(final String message){
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	try {	        		
	        		chatBox.appendText(message + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	   });
	}
	
	/**
	 * Clears the clientList
	 */
	protected static void clearClientList(){
		clientList.clear();
	}
	
	/**
	 * Displays the context menu for the table of connected users
	 */
	public static void tableClicked(){
		
		final ContextMenu contextMenu = new ContextMenu();
		
		MenuItem item1 = new MenuItem("Copy Username");
		item1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	StringSelection stringSelection = new StringSelection(connectedUsers.getSelectionModel().getSelectedItem().getUsername());
		    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    	clipboard.setContents(stringSelection, null);		    	
		    }
		});
	
		contextMenu.getItems().addAll(item1);
		
		//Popup menu for Connected Users table
		connectedUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent t) {
	            if(t.getButton() == MouseButton.SECONDARY)
	            {
	            	contextMenu.show(connectedUsers, t.getScreenX() , t.getScreenY());
	            }
	        }
	    });
	}
	
}
