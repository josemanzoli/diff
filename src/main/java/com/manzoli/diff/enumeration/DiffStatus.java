package com.manzoli.diff.enumeration;
/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Enum to handle the compare messages of the differences between the received JSON's
 * {@link #EQUAL}
 * {@link #DIFFERENT_SIZE}
 */
public enum DiffStatus {
	/** JSON's provided are equals */
	EQUAL("JSON's provided are equals"),
	/** JSON's provided have different sizes */
	DIFFERENT_SIZE("JSON's provided have different sizes");
	
	private String compare;
	
	DiffStatus(String compare){
		this.compare = compare;
	}
	
	public String getCompare(){
		return compare;
	}
}
