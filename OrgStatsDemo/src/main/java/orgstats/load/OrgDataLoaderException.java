package orgstats.load;

/**
 * Exception that occurs specifically when loading data into an OrgCollection.
 */
public class OrgDataLoaderException extends Exception {
	
	private static final long serialVersionUID = -3373117410332009451L;
	
	public OrgDataLoaderException(String message) {
        super(message);
    }
	
	public OrgDataLoaderException(String message, Throwable ex) {
        super(message, ex);
    }
}
