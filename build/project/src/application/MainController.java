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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Scanner;

import javax.crypto.Cipher;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controls the functions of the login screen for the chat room
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class MainController {
	
	private final static double rem = Math.rint(new Text("").getLayoutBounds().getHeight());
	
	@FXML private static TextField serverIPBox;
	@FXML private static TextField portBox;
	@FXML private static PasswordField passwordBox;
	@FXML private static TextField usernameBox;
	@FXML private static ImageView settingsButton;
	@FXML private static ImageView favoritesButton;
	@FXML private static Button connectButton;
	@FXML private static Label serverIPLbl;
	@FXML private static Label portLbl;
	@FXML private static Label passwordLbl;
	@FXML private static Label usernameLbl;
	
	//Server Variables//
	private static Socket socket = null; //The connection to the server
	private static ObjectInputStream ois = null; //Data being received from server
	private static ObjectOutputStream oos = null; //Data being sent TO server
	private static Object[] fromServer; //Holds data from server. A 2-dim array. [0] a String tag, to identify what it is. [1] the data to process
	//End Server Variables
	
	
	private static int version = 4;
	private static String versionStr = "v0.5.3";
	
	/**
	 * Sets the sizes of the components on the GUI
	 */
	public static void setup(){
	     
	     Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	try {
		        		
		        		serverIPLbl.setPrefSize(10.8 * rem, 1.34 * rem);
		        		serverIPLbl.setLayoutX(0.85 * rem);
		        		serverIPLbl.setLayoutY(1.55 * rem);
		        		serverIPLbl.setFont(Methods.lblFont);
		        		
		        		serverIPBox.setPrefSize(10.8 * rem, 1.34 * rem);
		        		serverIPBox.setLayoutX(0.85 * rem);
		        		serverIPBox.setLayoutY(3.1 * rem);
		        		
		        		portLbl.setPrefSize(10.8 * rem, 1.34 * rem);
		        		portLbl.setLayoutX(13.45 * rem);
		        		portLbl.setLayoutY(1.55 * rem);
		        		portLbl.setFont(Methods.lblFont);
		        		
		        		portBox.setPrefSize(10.8 * rem, 1.34 * rem);
		        		portBox.setLayoutX(13.45 * rem);
		        		portBox.setLayoutY(3.1 * rem);
		        		
		        		passwordLbl.setPrefSize(10.8 * rem, 1.34 * rem);
		        		passwordLbl.setLayoutX(0.85 * rem);
		        		passwordLbl.setLayoutY(4.85 * rem);
		        		passwordLbl.setFont(Methods.lblFont);
		        		
		        		passwordBox.setPrefSize(10.8 * rem, 1.34 * rem);
		        		passwordBox.setLayoutX(0.85 * rem);
		        		passwordBox.setLayoutY(6.4 * rem);
		        		
		        		usernameLbl.setPrefSize(10.8 * rem, 1.34 * rem);
		        		usernameLbl.setLayoutX(13.45 * rem);
		        		usernameLbl.setLayoutY(4.85 * rem);
		        		usernameLbl.setFont(Methods.lblFont);
		        		
		        		usernameBox.setPrefSize(10.8 * rem, 1.34 * rem);
		        		usernameBox.setLayoutX(13.45 * rem);
		        		usernameBox.setLayoutY(6.4 * rem);
		        		
		        		settingsButton.setScaleX(0.06 * rem);
		        		settingsButton.setScaleY(0.06 * rem);
		        		settingsButton.setLayoutX(0.85 * rem);
		        		settingsButton.setLayoutY(8.47 * rem);
		        		
		        		favoritesButton.setScaleX(0.06 * rem);
		        		favoritesButton.setScaleY(0.06 * rem);
		        		favoritesButton.setLayoutX(3.5 * rem);
		        		favoritesButton.setLayoutY(8.47 * rem);
		        		
		        		connectButton.setPrefSize(23.4 * rem, 1.34 * rem);
		        		connectButton.setLayoutX(0.85 * rem);
		        		connectButton.setLayoutY(10.65 * rem);
		        		
		       	     
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }
		   });
	     
	     
	     serverIPBox.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
				@Override public void handle(javafx.scene.input.KeyEvent e){
					if(e.getCode() == KeyCode.ENTER){
						connectClicked();
					}
				}
		 });
	     
	     portBox.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
				@Override public void handle(javafx.scene.input.KeyEvent e){
					if(e.getCode() == KeyCode.ENTER){
						connectClicked();
					}
				}
		 });
	     
	     passwordBox.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
				@Override public void handle(javafx.scene.input.KeyEvent e){
					if(e.getCode() == KeyCode.ENTER){
						connectClicked();
					}
				}
		 });
	     
	     usernameBox.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
				@Override public void handle(javafx.scene.input.KeyEvent e){
					if(e.getCode() == KeyCode.ENTER){
						connectClicked();
					}
				}
		 });
	     
	     readDefaults();
	     checkNewVersion();
	}
	
	/**
	 * Connects to a server when chosen from the Favorites
	 */
	protected static void favConnect(Favorite selection){
		serverIPBox.setText(selection.getIP());
		portBox.setText(selection.getPort());
		passwordBox.setText(selection.getPassword());
		usernameBox.setText(selection.getUsername());
		connectClicked();
	}
	
	/**
	 * Checks for a new version of the program
	 */
	private static void checkNewVersion() {
		Runnable rCheck = new Runnable(){			
			@Override
			public void run() {
			
				URL link = null;
				try {
					link = new URL("http://shayconcepts.com/programming/ChatClient/version.txt");
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}

				Scanner sc = null;
				try {
					sc = new Scanner(link.openStream(), "UTF-8");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				//This section converts the html page to the string
				String contents = "";
				while(sc.hasNextLine() || sc.hasNext()){
					contents = contents + sc.nextLine(); //Contents is the html page as a string!
				}
				sc.close();//Closes the scanner file
				
				String[] data = new String[2];
				data = contents.split(",");
				
				if(Integer.parseInt(data[0]) > getVersion()){
					
					UpdateController.setUpdateLink(data[1]);
					Platform.runLater(new Runnable() {
				        @Override
				        public void run() {
				        	try {
								new Update().start(new Stage());
							} catch (Exception e) {
								Methods.loadPopup("Failed to launch Update window", PopupController.ERROR, Main.getPopX(), Main.getPopY());
								e.printStackTrace();
							}
				        }
				     });
				}
			}	
		};
		
		Thread tCheck = new Thread(rCheck);
		tCheck.start();
		
	}
	
	/**
	 * Verifies that the username is the proper length
	 * @return T or F - If username is verified
	 */
	private static boolean verifyUsername(){
		if(usernameBox.getText().length() > 25){
			Methods.loadPopup("Your username cannot be longer than 25 characters. Remove " + (usernameBox.getText().length() - 25) +" characters.", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Runs when the Connect button is clicked
	 */
	public static void connectClicked(){
		Thread tConnect = new Thread(new Runnable(){
			@Override
			public void run() {
				setButtonWaiting();
				Crypto.generateRSAKeys();
				
				setDefaults();
				
				boolean verified = verifyUsername();
				if(verified){
					connectToServer();
				}			
			}			
		});
		
		tConnect.start();
		
	}
	
	/**
	 * Connects to the server
	 */
	 private static void connectToServer(){
		 Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		 boolean listen = true; //Listen for response from server
		 //Set up the connections to the Server
		try{
			socket = new Socket(getGUIAddress(), Integer.valueOf(getGUIPort()));
		}
		catch(Exception e){
			Methods.loadPopup("Failed to connect to server", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			listen = false;
			e.printStackTrace();
		}
		
		//Data being received from Server
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			Methods.loadPopup("Failed to connect to server", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			listen = false;
			e.printStackTrace();
		}
		
		//Data being sent TO server
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			Methods.loadPopup("Failed to connect to server", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			listen = false;
			e.printStackTrace();
		}
		
		boolean accept = false; //Whether or not the server accepted client
		
		//A constant loop that listens for data from the client
		while(listen){
			try{
				if((fromServer = (Object[]) ois.readObject()) != null){
					//Determine what data this is
					String tag = (String)fromServer[0]; //Getting the tag
				
					//Client has been accepted to server and can start to chat
					if(tag.equals("ACCEPT")){
						listen = false;
						accept = true;
					}
					
					//Incoming connections are being blocked by the server
					else if(tag.equals("BLOCK")){
						Methods.loadPopup("Server is blocking new connections", PopupController.INFO, Main.getPopX(), Main.getPopY());
						listen = false;
						accept = false;
						setButtonConnect();
					}
					
					//Encrypt the password that the user typed with the server's public key and send it to the server
					else if(tag.equals("GETPASS")){	
						byte[] input = getGUIPassword().getBytes();
					    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
					    SecureRandom random = new SecureRandom();
					    Key publicKey = (Key)fromServer[1];
					    cipher.init(Cipher.ENCRYPT_MODE, publicKey, random);
					    byte[] cipherText = cipher.doFinal(input);
					    
					    Object[] data = new Object[2];
					    data[0] = "PASS";
					    data[1] = cipherText;
					    sendData(data);
					}
					
					//Send the server the Client's public RSA key
					else if(tag.equals("GETPUBLICKEY")){
						Object[] data = new Object[2];
						data[0] = "KEY";
						data[1] = Crypto.getPublicKey();
						sendData(data);
					}
					
					//Decrypt the AES key from server
					else if(tag.equals("AES")){
						Cipher cipher = null;
						byte[] plainText = null;
						try {
							cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
							cipher.init(Cipher.DECRYPT_MODE, Crypto.getPrivateKey());		
							plainText = cipher.doFinal((byte[]) fromServer[1]);
						} 
						catch (Exception e) {
							Methods.loadPopup("Error decrypting AES key", PopupController.INFO, Main.getPopX(), Main.getPopY());
							listen = false;
							e.printStackTrace();
						}
						
						Crypto.setAesKey(plainText);
						
						//Tell server that Client is ready to start chatting
						Object[] data = new Object[1];
						data[0] = "READY";
						sendData(data);					
	
					}
					
					/*
					//Sends the username to the server when waiting to be approved
					else if(tag.equals("USERNAME")){
						Object[] data = new Object[2];
						data[0] = "USERNAME";
						data[1] = Crypto.encryptMessage(getGUIUsername());
						sendData(data);	
					}*/
					
					else if(tag.equals("BAN")){
						listen = false;
						accept = false;
						Methods.loadPopup("You have been banned from server", PopupController.ERROR, Main.getPopX(), Main.getPopY());
						setButtonConnect();
					}
					
					//Display an error from the server
					else if(tag.equals("ERROR")){
						listen = false;
						accept = false;
						Methods.loadPopup((String)fromServer[1], PopupController.ERROR, Main.getPopX(), Main.getPopY());
						setButtonConnect();
					}
				}
			}
			catch(EOFException e){
				e.printStackTrace();
			}
			catch(SocketException e){
				listen = false;
				Methods.loadPopup("Connection lost", PopupController.INFO, Main.getPopX(), Main.getPopY());
			}
			catch(Exception e){
				listen = false;
				Methods.loadPopup("Connection closed", PopupController.INFO, Main.getPopX(), Main.getPopY());
				e.printStackTrace();
			}
		}
		
		//Being chatting
		if(accept == true){
			byte[] iv = Crypto.getIV();
			Object[] idData = new Object[3]; //Setup to send ID
			idData[0] = "Data"; //The tag for the ServerMultiClient to read
			idData[1] = Crypto.encryptMessage(getGUIUsername(), iv); //The information (the ID of the client)
			idData[2] = iv;
			sendData(idData);
			
			setButtonConnect();
			//Load chat window and start reading in data from the server continuously
			loadChatRoom();		
		}
			
     }
	 
	 /**
	  * Sets the text of the button to the Waiting message
	  */
	 private static void setButtonWaiting(){
		 //connectButton.setText("Please wait...");
		 Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
	        		connectButton.setText("Please wait...");
		        }
		     });
		 
	 }
	 
	 /**
	  * Sets the text of the button to Connect
	  */
	 private static void setButtonConnect(){
		 Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
	        		connectButton.setText("Connect");

		        }
		     });
	 }
	 
	 
	 /**
	  * Disconnects from server
	  */
	 protected static void disconnect(){
		 ChatRoom.getPrimaryStage().close();
		 try {
			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		Main.getPrimaryStage2().show();
	 }
	 
	 /**
     * Sends data to the server
     * @param data
     */
    protected static void sendData(Object[] data){
    	try {
			oos.writeObject(data);
			oos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	 
	 /**
	  * Loads the chatroom
	  */
	 private static void loadChatRoom(){
    	Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	try {
					new ChatRoom().start(new Stage(), socket, oos, ois);
					Main.getPrimaryStage2().hide();
				} catch (Exception e) {
					Methods.loadPopup("Failed to launch chat room", PopupController.ERROR, Main.getPopX(), Main.getPopY());
					e.printStackTrace();
				}
	        }
	     });    	
	 }

	
	/**
	 * Returns the server address from the GUI
	 * @return serverIPBox.getText()
	 */
	private static String getGUIAddress(){
		return serverIPBox.getText();
	}
	
	/**
	 * Returns the port from the GUI
	 * @return portBox.getText()
	 */
	private static String getGUIPort(){
		return portBox.getText();
	}
	
	/**
	 * Returns the password from the GUI
	 * @return passwordBox.getText()
	 */
	private static String getGUIPassword(){
		return passwordBox.getText();
	}
	
	/**
	 * Returns the username from the GUI
	 * @return usernameBox.getText()
	 */
	private static String getGUIUsername(){
		return usernameBox.getText();
	}

	/**
	 * Returns the int version of the program
	 * @return
	 */
	private static int getVersion() {
		return version;
	}

	/**
	 * Returns the string version of the program
	 * @return
	 */
	protected static String getVersionStr() {
		return versionStr;
	}

	
	/**
	 * Displays the settings button context menu
	 */
	public static void settingsClicked(){
		final ContextMenu contextMenu = new ContextMenu();
		
		MenuItem item1 = new MenuItem("About");
		item1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        	try {
							new About().start(new Stage());
						} catch (Exception e) {
							Methods.loadPopup("Failed to launch About window", PopupController.ERROR, Main.getPopX(), Main.getPopY());
							e.printStackTrace();
						}
			        }
			     });
		    }
		});
		
		MenuItem item2 = new MenuItem("Help");
		item2.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		    	Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        	try {
							new Help().start(new Stage());
						} catch (Exception e) {
							Methods.loadPopup("Failed to launch Help window", PopupController.ERROR, Main.getPopX(), Main.getPopY());
							e.printStackTrace();
						}
			        }
			     });
		    }
		});
		
		contextMenu.getItems().addAll(item1, item2);
		
		contextMenu.show(settingsButton, Side.BOTTOM, 0, 0);
	}
	
	/**
	 * Runs when the Favorites button is clicked
	 */
	public static void favoritesClicked(){
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	try {
					new Favorites().start(new Stage());
				} catch (Exception e) {
					Methods.loadPopup("Failed to launch Favorites window", PopupController.ERROR, Main.getPopX(), Main.getPopY());
					e.printStackTrace();
				}
	        }
	     });
	}
	
	/**
	 * Reads defaults settings for the program
	 */
	private static void readDefaults(){
		checkDefaults();
		File defaultFile = checkDefaults();
		
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(defaultFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			serverIPBox.setText(br.readLine());
			portBox.setText(br.readLine());
			passwordBox.setText(br.readLine());
			usernameBox.setText(br.readLine());

			br.close();
			in.close();
			fstream.close();
			
		} catch (Exception e) {
		     Methods.loadPopup("Could not read default settings file", PopupController.ERROR, Main.getPopX(), Main.getPopY());

			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Checks to see if the default settings file exists
	 */
	private static File checkDefaults(){
		//Check to see if the file exists
		File defaultFile = new File(Methods.getProgramPath() + "defaults.txt");
		if(!defaultFile.exists()){
			try {
				defaultFile.createNewFile();
				FileWriter fstream = new FileWriter(defaultFile);
				BufferedWriter out = new BufferedWriter(fstream);
				
				out.write(""); //IP
				out.newLine();
				out.write(""); //Port
				out.newLine();
				out.write(""); //Password
				out.newLine();
				out.write(""); //Username
				
				out.close();
				fstream.close();
			} catch (IOException e) {
			     Methods.loadPopup("Could not create default settings file", PopupController.ERROR, Main.getPopX(), Main.getPopY());
				e.printStackTrace();
			}
		}
		
		return defaultFile;
	}
	
	/**
	 * Sets the default settings file
	 */
	private static void setDefaults(){
		File defaultFile = new File(Methods.getProgramPath() + "defaults.txt");
		try {
			defaultFile.createNewFile();
			FileWriter fstream = new FileWriter(defaultFile);
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write(serverIPBox.getText()); //IP
			out.newLine();
			out.write(portBox.getText()); //Port
			out.newLine();
			out.write(passwordBox.getText()); //Password
			out.newLine();
			out.write(usernameBox.getText()); //Username

			
			out.close();
			fstream.close();
		} catch (IOException e) {
		     Methods.loadPopup("Could not write default settings file", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Returns the title for the Chatroom window
	 * @return
	 */
	protected static String getChatTitle(){
		return serverIPBox.getText() + " - " + usernameBox.getText();
	}

}
