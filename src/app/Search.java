package app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.AllUsers;

public class Search 
{
	public static final int OR = 0;
	public static final int AND = 1;
	
	/**
	 * 
	 * @param tagName - tag name
	 * @param tagValue - tag value
	 * @return returns a List of Photo(s) having provided tagName+tagValue
	 * 		   in all the Album(s) of the current User
	 * 		   otherwise returns null
	 *  
	 */
	public static List<Photo> searchBy_1_Tag(String tagName, String tagValue)
	{
		User user = AllUsers.getCurrUser();
		List<Photo> filteredPhotos = new ArrayList<Photo>();
		for(Album album : user.getAllAlbumsList())
		{
			for(Photo p : album.getAllPhotosList())
			{				
				if(p.getTags().containsKey(tagName+tagValue))
					filteredPhotos.add(p);
			}
		}
		return filteredPhotos;
	}
	
	/**
	 * 
	 * @param tagName1 - tag name 1
	 * @param tagValue1 - tag value 1
	 * @param combination 0 for OR and 1 for AND
	 * @param tagName2 - tag name 2
	 * @param tagValue2 - tag value 2
	 * @return returns the List of Photo(s) with the combination(OR/AND) of the 2 tags
	 */
	public static List<Photo> searchBy_2_Tags(String tagName1, String tagValue1, 
			int combination, String tagName2, String tagValue2)
	{
		List<Photo> p1, p2;
		p1 = searchBy_1_Tag(tagName1, tagValue1);
		p2 = searchBy_1_Tag(tagName2, tagValue2);
		
		if(combination == OR)
		{
			p1.addAll(p2);
			return p1;
		}
		else if(combination == AND)
		{
			p1.retainAll(p2);
			return p1;
		}
		
		//returns null if --> int combination is NOT 0(OR) or 1(AND)
		return null;
	}
	
		
	/**
	 * 
	 * @param c1 Starting date (Calendar object)
	 * @param c2 End date (Calendar object)
	 * @return returns the List of Photo(s) fall into c1 to c2 date range(inclusive)
	 */
	public static List<Photo> searchByDateRange(Calendar c1, Calendar c2)
	{
		User user = AllUsers.getCurrUser();
		
		if(c1.after(c2)) return null;
		
		List<Photo> filteredPhotos = new ArrayList<Photo>();
		for(Album album : user.getAllAlbumsList())
		{
			for(Photo p : album.getAllPhotosList())
			{				
				if(!(p.getCal().before(c1) || p.getCal().after(c2)))
					filteredPhotos.add(p);
			}
		}
		return filteredPhotos;		
	}
	
}
