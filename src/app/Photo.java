/*
Author: Manohar Chitoda
date: ${date}
*/
package app;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Photo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar cal;
	private String caption;
	private String path;
	private Map<String, Tag> tags;
	
	/**
	 * @param cal - calendar instance
	 * @param caption - a small description
	 * @param path - path of the file
	 */
 	public Photo(Calendar cal, String caption, String path)
	{
		this.cal = cal;
		this.cal.set(Calendar.MILLISECOND, 0);
		this.caption = caption;
		this.path = path;
		this.tags = new HashMap<String, Tag>();
		tags.put("Person-", new Tag("Person", "-"));
		tags.put("Location-", new Tag("Location", "-"));
	}

 	/**
 	 * creates a new Photo from an existing Photo p
 	 * @param p - photo instance
 	 */
 	public Photo(Photo p)
 	{
 		this.cal = p.cal;
 		this.caption = p.caption;
 		this.path = p.path;
 		this.tags = new HashMap<String, Tag>();
 		this.tags.putAll(p.tags);
 	}
 	
 	//************* Setters & Getters *********************
 	
 	/**
 	 * 
 	 * @return returns the Calendar instance
 	 */
 	public Calendar getCal()
 	{
 		return this.cal;
 	}
 	
	/**
	 * 
	 * @return the hour in 24hr format
	 */
	public int getHour()
	{
		return this.cal.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 
	 * @return int - the minimum
	 */
	public int getMin()
	{
		return this.cal.get(Calendar.MINUTE);
	}
	
	/**
	 * 
	 * @return int - the seconds
	 */
	public int getSec()
	{
		return this.cal.get(Calendar.SECOND);
	}
	
	/**
	 * 
	 * @param hour - hour
	 * @param min - minutes
	 * @param sec - seconds
	 * @return true if valid hour, min and sec otherwise false
	 */
	public boolean setTime(int hour, int min, int sec) 
	{
		if(hour > 23 || hour < 0 || min > 59 || min < 0 || sec > 59 || sec < 0)
			return false;
		
		this.cal.set(Calendar.HOUR_OF_DAY, hour);
		this.cal.set(Calendar.MINUTE, min);
		this.cal.set(Calendar.SECOND, sec);
		return true;
	}

	/**
	 * 
	 * @return int - the day of the month
	 */
	public int getDay()
	{
		return this.cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 
	 * @return int - the month
	 */
	public int getMonth()
	{
		return this.cal.get(Calendar.MONTH);
	}
	
	/**
	 * 
	 * @return int - the year
	 */
	public int getYear()
	{
		return this.cal.get(Calendar.YEAR);
	}
	
	/**
	 * 
	 * @param year - the year
	 * @param month - month
	 * @param day - day
	 * @return true if the day, month and year fields are valid and assigned 
	 *         otherwise false
	 */
	public boolean setDate(int year, int month, int day)
	{
		if(year > 2019 || year < 0 || month > 11 || month < 0 || day > 31 || day < 1)
			return false;
		if(!day_month_combination(day, month, year)) return false;
		
		this.cal.set(Calendar.YEAR, year);
		this.cal.set(Calendar.MONTH, month);
		this.cal.set(Calendar.DAY_OF_MONTH, day);
		return true;
	}
	
	/**
	 * 
	 * @param year - the year
	 * @param month - month
	 * @param day - day
	 * @return true if day, month and year combination are correct
	 */
	private boolean day_month_combination(int day, int month, int year)
	{
		if(day < 29) return true;
		
		if(day == 31)
		{
			switch(month) 
			{
				case 1: case 3: case 5:
				case 7: case 8: case 10: case 12:
					return true;
					
				default: return false;
			}
		}
		else if(day == 30)
		{
			switch(month) 
			{
				case 4: case 6: case 9: case 11:
					return true;
					
				default: return false;
			}
		}
		
		if(month == 2)
		{
			if(year % 100 == 0 && year % 400 != 0) return false;
			else if(year % 4 == 0) return true;
			else return false;
		}
		return true;
	}

	/**
	 * @return String - the caption
	 */
	public String getCaption() 
	{
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	/**
	 * @return String -the path of the photo
	 */
	public String getPath() 
	{
		return this.path;
	}

	/**
	 * @param path - path of the photo
	 */
	public void setPath(String path) 
	{
		this.path = path;
	}

 	//************* Setters & Getters *********************
	
	/**
	 * 
	 * @param tagName - tag name
	 * @param tagValue - its value
	 * @return true if the tag-value pair is unique and is added to the 
	 *         tags (Map) successfully otherwise false
	 */
	public boolean addTag(String tagName, String tagValue)
	{
		Tag t = new Tag(tagName, tagValue);
		
		return (this.tags.putIfAbsent((t.getTagName()+t.getTagValue()), t) == null);
	}

	/**
	 * 
	 * @param tagName - tag name
	 * @param tagValue - its value
	 * @return true if tag with tagName and tagValue exist and is removed
	 * 			otherwise returns false
	 */
	public boolean removeTag(String tagName, String tagValue)
	{
		return(this.tags.remove(tagName + "" + tagValue) != null);
	}
	
	/**
	 * 
	 * @return returns the Map of Tags 
	 *        "tagName+tagValue" is mapped to Tag objects
	 */
	public Map<String, Tag> getTags()
	{
		return this.tags;
	}


}