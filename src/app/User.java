
package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private Map<String, Album> albums;
	private static Album copiedAlbum;
	
	/**
	 * @param username - this is the username when a user is created
	 * */
	public User(String username) 
	{
		this.username = username;
		this.albums = new HashMap<String, Album>();
	}
	
	/**
	 * 
	 * @return returns the copiedAlbum object
	 */
	public static Album getCopiedAlbum()
	{
		return copiedAlbum;
	}
	
	/**
	 * 
	 * @param p, assigns the Album p to the copiedAlbum object
	 */
	public static void setCopiedAlbum(Album p)
	{
		copiedAlbum = p;
	}
	
	/**
	 * 
	 * @param albumName the name of the album
	 * @return returns the Album object with the album name as albumName
	 * 			returns null if Album does not exist
	 */
	public Album getAlbum(String albumName)
	{
		return (this.albums.get(albumName));
	}
	
	/**
	 * 
	 * @return returns the List of Albums for this user
	 */
	public List<Album> getAllAlbumsList()
	{
		List<Album> allAlbumsList = new ArrayList<Album>();
		for(String albumName : this.albums.keySet())
		{
			allAlbumsList.add(this.albums.get(albumName));
		}
		return allAlbumsList;
	}
	
	/**
	 * 
	 * @param a, new Album object to be added in the User object
	 * @return true if added successfully else returns false
	 */
	public boolean addAlbum(Album a)
	{
		if(a == null) return false;
		return (this.albums.putIfAbsent(a.getName(), a) == null);
	}
	
	/**
	 * 
	 * @param albumName - name of the album
	 * @return true if album name is unique and added to the Albums of the User
	 *         false otherwise
	 */
 	public boolean createAlbum(String albumName)
	{
		return (this.albums.putIfAbsent(albumName, new Album(albumName)) == null);
	}
	
 	/**
 	 * 
 	 * @param albumName - name of the album
 	 * @return returns true if the album with album name as albumName
 	 *         exist and is deleted
 	 *         false is it does not exist
 	 */
	public boolean deleteAlbum(String albumName)
	{
		return (this.albums.remove(albumName) != null);
	}
	
	/**
	 * 
	 * @param currName the current name
	 * @param newName change the current name to this name
	 * @return true if an album with currName exist and 
	 *         album with newName does NOT exist
	 *         otherwise returns false
	 */
	public boolean editAlbumName(String currName, String newName)
	{
		Album a = this.getAlbum(currName);
		Album b = this.getAlbum(newName);
		
		//false if currAlbum is null or an album with newName already exist
		if(a == null || b != null)
			return false;
		a.setName(newName);
		this.albums.put(newName, a);
		this.albums.remove(currName);
		return true;
	}
	
	/**
	 * 
	 * @param albumName, the album to be copied
	 */
	public void copyAlbum(String albumName)
	{
		copiedAlbum = new Album(this.getAlbum(albumName));
	}
	
	/**
	 * 
	 * @return true if the copied album is successfully pasted to the new location
	 *         otherwise false
	 */
	public boolean pasteAlbum()
	{
		if(copiedAlbum == null) return false;
		return (this.albums.putIfAbsent(copiedAlbum.getName(), copiedAlbum) == null);
	}
	
	/**
	 * @return string - the user name of this instance
	 * */
	public String getUsername()
	{
		return this.username;
	}
	
	/**
	 * @return string - the user name of this instance 
	 *      same as getUsername()
	 */
	public String toString()
	{
		return this.username;
	}

}
