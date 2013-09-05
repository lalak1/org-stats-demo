package orgstats;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a group of Organizations where each organization has users and 
 * a possibility to have no to many child organizations in a tree structure.
 * <p>
 * There can only be one organization in the entire tree and a user can
 * only be associated to one organization.
 */
public class OrgCollection {
	
	private static final Logger logger = Logger.getLogger(OrgCollection.class.getName());
	public List<OrgBean> orgList;
	
	/**
	 * Create a collection with no OrgBean elements.
	 */
	public OrgCollection() {
		orgList = new ArrayList<OrgBean>();
	}
		
	/**
	 * Adds an OrgBean to the OrgCollection and associates it to the correct parent,
	 * if it doesn't exist in the "tree" then the OrgBean is placed at the top level.
	 * 
	 * @param orgBean		the OrgBean to add to the collection.
	 */
	public void add(OrgBean orgBean) {
		
		// Organizations with a parentOrgId set to 0 are top level orgs.
		if (orgBean.getParentOrgId() == 0) {
			orgList.add(orgBean);				
		} else {
			// Find parent and add to it.
			OrgBean parentOrgBean = getOrg(orgBean.getParentOrgId());
			if (parentOrgBean != null) {
				parentOrgBean.getChildOrgs().add(orgBean);
			} else {
				// If parent doesn't exist, then assign to top of list to be reassigned later if it exists.
				orgList.add(orgBean);
			}
		}
		// Find any "dangling" children on top node to reattach to current parent.
		findAndMoveChildOrgsFromTopLevel(orgBean);
	}
	
	/**
	 * Returns the OrgBean for the given unique orgId not matter where it exists in 
	 * the organization tree.
	 * 
	 * @param orgId		the unique identifier for the OrgBean
	 * @return OrgBean	an OrgBean if it exists in the tree, otherwise null
	 */
	public OrgBean getOrg(int orgId) {
		return findOrgBean(orgId, this.orgList);
	}
		
	/**
	 * Retrieves all the child OrgBeans for a given orgId recursively through the tree
	 * The OrgBean specified by the orgId may or may not be included in the return list.
	 * 
	 * @param orgId			the unique identifier for the OrgBean
	 * @param inclusive		the designator to include the OrgBean identified by orgId
	 * @return				a list of all the children OrgBeans for the OrgBean for the orgId.  If 
	 * 						inclusive is true, the OrgBean identified by orgId is also returned in the list.  
	 * 						If there are no beans, an empty List is returned.
	 */
	public List<OrgBean> getOrgTree(int orgId, boolean inclusive) {
		
		List<OrgBean> orgBeans = new ArrayList<OrgBean>();
		OrgBean orgBean = getOrg(orgId);
		
		if (orgBean != null) {
			if (inclusive) {
				orgBeans.add(orgBean);
			}
			orgBeans.addAll(findAllChildOrgs(orgBean));
		}
		return orgBeans;
	}
	
	/**
	 * Adds a UserBean to the correct OrgBean in the org tree for the orgId specified
	 * on the UserBean.  
	 * 
	 * @param userBean		the UserBean that is added to the orgId specified on the bean
	 * @return boolean		true if UserBean was added to org bean false if the UserBean is null
	 * 						or the OrgBean cannot be found.
	 */
	public boolean addUser(UserBean userBean) {
		
		boolean userAdded = false;
		if (userBean != null) {
			OrgBean userOrg = getOrg(userBean.getOrgId());
			if (userOrg != null) {
				userOrg.getUsers().add(userBean);
				userAdded = true;
			} else {
				logger.log(Level.WARNING, "User not added to OrgBean.  Org doesn't exit." + userBean);
			}
		} else {
			logger.log(Level.WARNING, "UserBean not added to OrgBean.  UserBean is null.");
		}
		return userAdded;
	}
	
	/**
	 * Returns a list of OrgBeans for the top level OrgBeans.
	 * 
	 * @return		a list of OrgBeans that are at the top of the org tree.
	 */
	public List<OrgBean> getTopLevelOrgs() {
		return orgList;
	}
	
		
	/* Finds any "dangling" children on top node and reattaches it to correct OrgBean in tree.
	 * It is not necessary to go through entire tree as they are listed on the top level.
	 * 
	 * @param orgBean		the OrgBean that may be a parent to top level "dangling" child nodes
	 */
	private void findAndMoveChildOrgsFromTopLevel(OrgBean orgBean) {

		for (OrgBean currentOrgBean : orgList) {
			if (orgBean.getOrgId() == currentOrgBean.getParentOrgId()) {
				orgList.remove(currentOrgBean);
				orgBean.getChildOrgs().add(currentOrgBean);
				break;
			}
		}
	}
	
	/*
	 * Returns all the child OrgBeans and recursively their child OrgBeans
	 * for a partical orgBean.
	 * 
	 * @param orgBean	the OrgBean to retrieve child OrgBeans
	 * @return			a list of all child OrgBeans recursively
	 */
	private List<OrgBean> findAllChildOrgs(OrgBean orgBean) {
		
		List<OrgBean> childOrgs = new ArrayList<OrgBean>();
		for (OrgBean childOrg : orgBean.getChildOrgs()) {
			childOrgs.add(childOrg);
			childOrgs.addAll(findAllChildOrgs(childOrg));
		}
		return childOrgs;
	}
	
	/*
	 * Recursively finds the OrgBean identified by orgId starting from a level specified
	 * by the List of OrgBeans passed.
	 * 
	 * @param orgId			the unique identifier for the OrgBean to find
	 * @param orgBeans		the list of OrgBeans to start the search
	 * @return OrgBean		the OrgBean found in the tree identified by orgId, null if not found 
	 */
	private OrgBean findOrgBean(int orgId, List<OrgBean> orgBeans) {
		
		for (OrgBean currentOrgBean : orgBeans) {
			if (orgId == currentOrgBean.getOrgId()) {
				return currentOrgBean;
			} else {
				// Check children
				currentOrgBean = findOrgBean(orgId, currentOrgBean.getChildOrgs());
				if (currentOrgBean != null) {
					return currentOrgBean;
				}
			}
		}
		return null;
	}
	
}
