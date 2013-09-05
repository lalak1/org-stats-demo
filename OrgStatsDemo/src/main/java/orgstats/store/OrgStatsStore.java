package orgstats.store;

import orgstats.OrgCollection;

/**
 * Data store for organizational hierarchy usage statistics.
 */
public interface OrgStatsStore {

	/**
	 * Stores organization, user, and file usage data from memory.
	 * 
	 * @return orgCollection		the OrgCollection containing a tree of OrgBeans			
	 */
	void storeData(OrgCollection orgCollection) throws OrgStatsStoreException;

}
