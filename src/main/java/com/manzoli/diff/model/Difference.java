package com.manzoli.diff.model;

/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Model class for mongoDb Database containing the differences found by the application itself.
 */
public class Difference {
	
	/** Name of the field where the difference are. 
	 * If there is no information inside one of the fields {@link #rightSize}/{@link #leftSize} its because the field exists in only one JSON 
	*/
	private String field;
	
	/** The size of the field inside the right JSON */
	private Integer rightSize;
	
	/** The size of the field inside the left JSON */
	private Integer leftSize;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Integer getRightSize() {
		return rightSize;
	}
	public void setRightSize(Integer rightSize) {
		this.rightSize = rightSize;
	}
	public Integer getLeftSize() {
		return leftSize;
	}
	public void setLeftSize(Integer leftSize) {
		this.leftSize = leftSize;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((leftSize == null) ? 0 : leftSize.hashCode());
		result = prime * result + ((rightSize == null) ? 0 : rightSize.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Difference other = (Difference) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (leftSize == null) {
			if (other.leftSize != null)
				return false;
		} else if (!leftSize.equals(other.leftSize))
			return false;
		if (rightSize == null) {
			if (other.rightSize != null)
				return false;
		} else if (!rightSize.equals(other.rightSize))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Difference [field=" + field + ", rightSize=" + rightSize + ", leftSize=" + leftSize + "]";
	}
	
}
