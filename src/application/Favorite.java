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

/**
 * Used for listing the servers information in the table for favorites
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
public class Favorite {

	private String IP;
	private String port;
	private String password;
	private String username;
	private String passSet = "NO"; //This string will contain YES or NO - Telling the user whether or not their favorite has a password set
	
	public Favorite(String IP, String port, String password, String username) {
		this.IP = IP;
		this.port = port;
		this.password = password;
		this.username = username;
		
		if(!password.equals("")){
			this.passSet = "YES";
		}
		
	}
	
	/**
	 * Determines if a selection is equal
	 * @param cmp
	 * @return t/f
	 */
	public boolean equals(Favorite cmp){
		if(getIP().equals(cmp.getIP()) && getPort().equals(cmp.getPort()) && getPassword().equals(cmp.getPassword()) && getUsername().equals(cmp.getUsername())){
			return true;
		}
		
		return false;
	}

	/**
	 * Gets IP
	 * @return IP
	 */
	public String getIP() {
		return IP;
	}

	/**
	 * Sets IP
	 * @param iP
	 */
	public void setIP(String iP) {
		IP = iP;
	}

	/**
	 * Gets port
	 * @return port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Sets port
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Returns password
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Gets usernmae 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns if YES or NO - States whether a password has been set
	 * @return passSet
	 */
	public String getPassSet() {
		return passSet;
	}

}
