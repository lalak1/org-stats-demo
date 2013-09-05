package orgstats.load;

import orgstats.OrgCollection;

/**
 * Loader for creating a collection of organizations from a data source(s).
 */
public interface OrgDataLoader {

	/**
	 * Populates organization, user, and file usage data into an OrgCollection.
	 * 
	 * @return orgCollection		the OrgCollection containing a tree of OrgBeans			
	 */
	OrgCollection loadData() throws OrgDataLoaderException;
}
