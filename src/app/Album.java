package app;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Album implements Serializable
{
	private String name;
	private Map<String, Photo> photos;
	private static Photo copiedPhoto;
	private static Album copiedPhotoFrom;
	
	/**
	 * @param name the name of the album
	 */
	public Album(String name)
	{
		this.name = name;
		this.photos = new HashMap<String, Photo>();
	}
	
	/**
	 * DEEP COPY
	 * @param a, new album created using album 'a'
	 */
	public Album(Album a)
	{
		this.name = a.name;
		this.photos = new HashMap<String, Photo>();
		this.photos.putAll(a.photos);
	}
	
	/**
	 * DEEP COPY
	 * creates a new Album object using the provided
	 *  albumName name, and
	 *  list of photos
	 * @param name ~ name of the album
	 * @param photos ~ list of photos in album
	 */
	public Album(String name, List<Photo> photos)
	{
		this(name);
		this.photos = new HashMap<String, Photo>();
		
		for(Photo p : photos)
		{
			this.photos.put(p.getPath(), p);
		}
	}
	
	//************* Setters & Getters *********************
	
	/**
	 * @param name the name of the album
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @param photoPath ~the file path of the photo
	 * @return the Photo object with the specified photoPath if exist
	 * 		   otherwise null
	 */
	public Photo getPhoto(String photoPath)
	{
		return this.photos.get(photoPath);
	}
	
	/**
	 * @return returns the copiedPhoto object
	 */
	public static Photo getCopiedPhoto()
	{
		return copiedPhoto;
	}
	
	/**
	 * 
	 * @return returns the copiedPhotoFrom object
	 */
	public static Album getCopiedPhotoFrom()
	{
		return copiedPhotoFrom;
	}
	
	/**
	 * 
	 * @param p, assigns the Photo p to copiedPhoto object
	 */
	public static void setCopiedPhoto(Photo p)
	{
		copiedPhoto = p;
	}
	
	/**
	 * 
	 * @param p, assigns the Album p to copiedPhotoFrom object
	 */
	public static void setCopiedPhotoFrom(Album p)
	{
		copiedPhotoFrom = p;
	}
	
	/**
	 * 
	 * @return returns a List of all the Photos in this Album
	 */
	public List<Photo> getAllPhotosList()
	{
		List<Photo> allPhotosList = new ArrayList<Photo>();
		for(String photoPath : this.photos.keySet())
		{
			allPhotosList.add(this.photos.get(photoPath));
		}
		return allPhotosList;
	}
	
	/**
	 * 
	 * @return returns null if Album has NO photos otherwise
	 * 			int [][]
	 * 			int[0][0-2] stores day, month, year (Range Start)
	 * 			int[1][0-2] stores day, month, year (Range End)		
	 */
	public int[][] getDateRange()
	{
		if(this.photos.isEmpty()) return null;
		
		//int[0][0-2] --> stores day, month, year --> Range Start
		//int[1][0-2] --> stores day, month, year --> Range End		
		int [][] dateRange = new int[2][3];
		
		//minDate --> set to Max value --> will get the Min(earliest) date
		Calendar minDate = Calendar.getInstance();
		minDate.setTime(new Date(Long.MAX_VALUE));
		
		//maxDate --> set to Min value --> will get the Max(latest) date
		Calendar maxDate = Calendar.getInstance();
		maxDate.setTime(new Date(Long.MIN_VALUE));
		
		for(Photo p : this.getAllPhotosList())
		{
			Calendar temp = p.getCal();
			if(temp.before(minDate)) minDate = temp;
			if(temp.after(maxDate)) maxDate = temp;
		}
		
		//Range Start --> earliest date
		dateRange[0][0] = minDate.get(Calendar.DAY_OF_MONTH);
		dateRange[0][1] = minDate.get(Calendar.MONTH) + 1;
		dateRange[0][2] = minDate.get(Calendar.YEAR);
		
		//Range End --> latest date
		dateRange[1][0] = minDate.get(Calendar.DAY_OF_MONTH);
		dateRange[1][1] = minDate.get(Calendar.MONTH) + 1;
		dateRange[1][2] = minDate.get(Calendar.YEAR);
		
		return dateRange;
	}
	
	//************* Setters & Getters *********************
	
	/**
	 * 
	 * @param p ~ a Photo object passed to add to the album
	 * @return true if Photo p is added to this Album succesfully
	 * 			otherwise false
	 */
	public boolean addPhoto(Photo p)
	{
		return (this.photos.putIfAbsent(p.getPath(), p) == null);
	}
	
	/**
	 * 
	 * @param photoPath ~the file path of the photo
	 * @return true if the Photo with associated photoPath exist and is deleted
	 * 			otherwise returns false
	 */
	public boolean deletePhoto(String photoPath)
	{
		return (this.photos.remove(photoPath) != null);
	}
	
	/**
	 * creates a new Photo object for the copied photo to be pasted
	 *  in some other Album
	 *  
	 * @param photoPath ~the file path of the photo
	 * @return true if copied successfully otherwise false
	 */
	public boolean copyPhoto(String photoPath)
	{
		//copying will not alter the source album of the photo
		copiedPhotoFrom = null;
		
		copiedPhoto = this.photos.get(photoPath);
		if(copiedPhoto == null) return false;
		
		copiedPhoto = new Photo(copiedPhoto);
		return true;
	}
	
	/**
	 * creates a new Photo object for the copied/moved photo to be pasted
	 *  in some other Album, keeping track of its current location
	 *  
	 * @param photoPath ~  the path of the photo
	 * @return boolean ~ True always, but false when if photo is not coppied
	 */
	public boolean movePhoto(String photoPath)
	{
		//when photo is pasted, it will delete that photo from this Album 
		copiedPhotoFrom = this;
		
		copiedPhoto = this.photos.get(photoPath);
		if(copiedPhoto == null) return false;
		
		copiedPhoto = new Photo(copiedPhoto);
		
		return true;
	}
	
	/**
	 * 
	 * @return 0 for successful paste
	 * 		   1 if Photo already exist in the destination Album
	 * 		  -1 if Nothing to paste
	 */
	public int pastePhoto()
	{
		//-1 --> Nothing to paste
		if(copiedPhoto == null) return -1;
		
		//1 --> copiedPhoto already exist in the album
		if(!this.addPhoto(copiedPhoto)) return 1;
		
		//for copy --> copiedPhotoFrom = null
		//for move --> copiedPhotoFrom = source Album
		if(copiedPhotoFrom != null)
		{
			//removing photo from the source Album
			copiedPhotoFrom.deletePhoto(copiedPhoto.getPath());
		}

		//reset
		copiedPhoto = null;
		copiedPhotoFrom = null;
		
		//0 --> successful paste
		return 0;
	}
	/**
	 * @return String which displays the details of it
	 */
	@Override
	public String toString()
	{
		int[][] dtr = this.getDateRange();
		String dateRange = "-";
		
		if(dtr != null ) 
		{
			dateRange = "" + dtr[0][0] + "/" + dtr[0][1] + "/" + dtr[0][2];
			dateRange += " - ";
			dateRange += dtr[0][0] + "/" + dtr[0][1] + "/" + dtr[0][2];
		}
				
		return String.format("%17s %10.10s %65s %4d %50s %s", "", this.name.trim(), "", this.photos.size(), "", dateRange);
	}
	
}
