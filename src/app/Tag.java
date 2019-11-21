package app;

import java.io.Serializable;

public class Tag implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tagName;
	private String tagValue;
	
	/**
	 * 
	 * @param tagName tag's name 
	 * @param tagValue the value of the tagName
	 */
	public Tag(String tagName, String tagValue) 
	{
		if(tagName == null || tagName.length() == 0) this.tagName = "-";
		else this.tagName = tagName.trim();
		
		if(tagValue == null || tagValue.length() == 0) this.tagValue = "-";
		else this.tagValue = tagValue.trim();
	}

	/**
	 * 
	 * @return returns the tag value
	 */
	public String getTagValue()
	{
		return this.tagValue;
	}
	
	/**
	 * 
	 * @return returns the tag name
	 */
	public String getTagName()
	{
		return this.tagName;
	}
	
	/**
	 * 
	 * @param tagName, new tagName to be set
	 */
	public void setTagName(String tagName)
	{
		if(tagName == null || tagName.length() == 0) this.tagName = "-";
		else this.tagName = tagName.trim();
	}
	
	/**
	 * 
	 * @param tagValue, new tagValue to be set
	 */
	public void setTagValue(String tagValue)
	{
		if(tagValue == null || tagValue.length() == 0) this.tagValue = "-";
		else this.tagValue = tagValue.trim();
	}
	
	/**
	 * @return returns tagName : tagValue
	 */
	public String toString()
	{
		return (this.tagName + " : " + this.tagValue); 
	}
}
