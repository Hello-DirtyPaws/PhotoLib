
package model;
import java.io.*;
import java.util.*;

import app.User;

/**
 * @author manoharchitoda
 * @author surajupadhyay
*/
public class AllUsers implements Serializable  
{
	private static final long serialVersionUID = 1L;
	private Map<String, User> users;
	private static final String storeFile = "src/model/users.dat"; 
	private static User currUser;
	private static AllUsers privateObj;
	
	/**
	 * default constructor
	 */
	private AllUsers() 
	{
		try
		{
			AllUsers obj = readApp();
			this.users = obj.users;
			currUser = null;
		}
		catch(Exception e)
		{
			//IO or ClassNotFound or FileNotFound Exception
			this.users = new HashMap<String, User>();
			currUser = null;
		}
	}
	/**
	 * @return object of AllUsers
	 * @throws IOException the loader might not be able to loader the FXML
	 * @throws ClassNotFoundException FileInputStream might not find the file
	 */
	private static AllUsers readApp() throws IOException, ClassNotFoundException 
	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeFile));
		AllUsers allUsers = (AllUsers)ois.readObject();
		ois.close();
		return allUsers;
	} 
	
	/**
	 * 
	 * @param allUsers contains the list(Map) of all the users
	 * @throws IOException the loader might not be able to loader the FXML
	 */
	public static void writeApp(AllUsers allUsers) throws IOException 
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile));
		oos.writeObject(allUsers);
		oos.close();
	}

	/**
	 * 
	 * @param username username of the user to be removed
	 * @return true if user found and deleted
	 */
	public boolean removeUser(String username)
	{
		username = username.trim().toLowerCase();
		if(this.users.remove(username) != null)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param username username of the new user to be added
	 * @return true if new user was added successfully
	 * 		   else false (a User with the same username already exist)
	 */
	public boolean addUser(String username)
	{
		username = username.trim().toLowerCase();
		User u = this.users.get(username);
		if(u != null) 
			return false;
		
		u = new User(username);
		this.users.put(username, u);
		return true;
	}
	
	/**
	 * 
	 * @param username username of the user to get the User object of
	 * @return returns the User object if exist otherwise null
	 */
	public User getUserByName(String username)
	{
		username = username.trim().toLowerCase();
		return (this.users.get(username));
	}
	
	/**
	 * 
	 * @return returns the current user if in session else returns null
	 */
	public static User getCurrUser()
	{
		return currUser;
	}
	
	/**
	 * 
	 * @param input initializes the currUser to the passed userName
	 */
	public static void setCurrUser(String input)
	{
		currUser = privateObj.getUserByName(input);
	}
	
	/**
	 * 
	 * @return returns the one and only AllUsers static object
	 */
	public static AllUsers getAllUsersObj()
	{
		if(privateObj != null)
		{
			return privateObj;
		}
		else
		{
			privateObj = new AllUsers();
			return privateObj;
		}
	}

	/**
	 * 
	 * @return returns the list (Map with type String userName and User) of all the users
	 */
	public Map<String, User> getUsersMap()
	{
		return this.users;
	}

	/**
	 * 
	 * @return returns a List of type User of all the users
	 */
	public List<User> getUsersList()
	{
		List<User> userList = new ArrayList<User>();
		
		for(String username : this.users.keySet())
		{
			userList.add(this.users.get(username));
		}		
		return userList;
	}
}
