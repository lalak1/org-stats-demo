package orgstats;

/**
 * Generic helper methods for Org Stats processing
 */
public class OrgStatsHelper {

	/**
	 * Returns whether or not the String is an integer (includes all data types, e.g. long)
	 * 
	 * @param stringVar		the String to test
	 * @return boolean		true if stringVar is an integer, otherwise false
	 */
	public static boolean isInteger(String stringVar) {
	    
		if (stringVar == null) {
			return false;
		}
		
		int size = stringVar.length();
		for (int i = 0; i < size; i++) {
	        if (!Character.isDigit(stringVar.charAt(i))) {
	            return false;
	        }
	    }
	    return size > 0;
	}
	
	/**
	 * Returns whether or not the String contains at least one letter or digit
	 * 
	 * @param stringVar		the String to test
	 * @return boolean		false if stringVar is null or doesn't contain a letter or digit
	 */
	public static boolean containsLetterOrDigit(String stringVar) {
		
		if (stringVar == null) {
			return false;
		}
		
		for (int i = 0; i < stringVar.length(); i++) {
	        if (Character.isLetterOrDigit(stringVar.charAt(i))) {
	            return true;
	        }
	    }
	    return false;
	}
}
