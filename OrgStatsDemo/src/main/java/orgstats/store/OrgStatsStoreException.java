package orgstats.store;

/**
 * Exception that occurs specifically when storing OrgCollection data.
 */
public class OrgStatsStoreException extends Exception {

	private static final long serialVersionUID = 837854356671052479L;

	public OrgStatsStoreException(String message) {
        super(message);
    }
	
	public OrgStatsStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
