package orgstats;

import java.util.ArrayList;
import java.util.List;

/**
 * Organization that contains users and children organizations and 
 * provides user usage rollup statistics.
 */
public class OrgBean implements Comparable<OrgBean> {

	private int orgId;
	private int parentOrgId;
	private String name;
	private final List<UserBean> users;
	private final List<OrgBean> childOrgs;
	
	/**
	 * Contructs organization with id only and empty child and user lists.
	 * ParentId defaults to 0 and the name defaults to null.
	 * 
	 * @param orgId			the unique identifier for the organization
	 */
	public OrgBean(int orgId) {
		this.orgId = orgId;
		this.childOrgs = new ArrayList<OrgBean>();
		this.users = new ArrayList<UserBean>();
	}
	
	/**
	 * Contructs organization with empty child and user lists.
	 * 
	 * @param orgId			the unique identifier for the organization.
	 * @param parentOrgId	the organizations parent unique identifier.
	 * @param name			the organization name
	 */
	public OrgBean(int orgId, int parentOrgId, String name) {
		this(orgId);
		this.parentOrgId = parentOrgId;
		this.name = name;
	}
		
	/**
	 * Returns the total number of users for the organization.
	 * This includes all users in child organizations, recursively.
	 * 
	 * @return	the total number of users in the organization, including child organizations.
	 */
	public int getTotalNumUsers() {
		
		int totalNumUsers = this.getUsers().size();
		
		for (OrgBean childOrg : this.getChildOrgs()) {
			totalNumUsers = totalNumUsers + childOrg.getTotalNumUsers();
		}		
		return totalNumUsers;
	}
	
	/**
	 * Returns the total number of files for the users in the organization.
	 * This includes all user file counts in child organizations, recursively.
	 * 
	 * @return	the total number of files for the entire organization, including child organizations
	 */
	public int getTotalNumFiles() {
		
		int totalNumFiles = 0;
		
		for (UserBean userBean : this.getUsers()) {
			totalNumFiles = totalNumFiles + userBean.getNumFiles();
		}
		for (OrgBean childOrg : this.getChildOrgs()) {
			totalNumFiles = totalNumFiles + childOrg.getTotalNumFiles();
		}		
		return totalNumFiles;
	}
	
	/**
	 * Returns the total number of bytes for the organization.
	 * This includes all bytes in child organizations, recursively.
	 * 
	 * @return	the total number of bytes for the entire organization, including child organizations.
	 */
	public long getTotalNumBytes() {
		
		long totalNumBytes = 0;
		
		for (UserBean userBean : this.getUsers()) {
			totalNumBytes = totalNumBytes + userBean.getNumBytes();
		}
		for (OrgBean childOrg : this.getChildOrgs()) {
			totalNumBytes = totalNumBytes + childOrg.getTotalNumBytes();
		}
		return totalNumBytes;	
	}

	public List<OrgBean> getChildOrgs() {
		return childOrgs;
	}
	
	public List<UserBean> getUsers() {
		return users;
	}
	
	public int getOrgId() {
		return orgId;
	}
	
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	
	public int getParentOrgId() {
		return parentOrgId;
	}
	
	public void setParentOrgId(int parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orgId;
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
		OrgBean other = (OrgBean) obj;
		if (orgId != other.orgId)
			return false;
		return true;
	}

	/**
	 * Compares two org beans by the organization id.
	 * 
	 * @param other		the OrgBean to be compared
	 * @return			the value 0 if this OrgBean's orgId is numerically equal to the argument's orgId;
	 * 					a value less than 0 if the OrgBean's orgId is numerically less than the argument's orgId;
	 * 					a value more than 0 if the OrgBean's orgId is numerically greater than the argument's orgId
	 */
	@Override
	public int compareTo(OrgBean other) {
		return Integer.valueOf(this.orgId).compareTo(Integer.valueOf(other.orgId));
	}
	
	@Override
	public String toString() {
		return "OrgBean [orgId=" + orgId + ", parentOrgId=" + parentOrgId
				+ ", name=" + name + "]";
	}
	
	
	/**
	 * Returns a String object that represents the organization by id 
	 * and usages statistics (total user, total files, total bytes).
	 * 
	 * @return	a string representation of the object with usage stats
	 */
	public String getStatsString() {
		return  name + "(" + orgId +")  Total Users: " + this.getTotalNumUsers() 
				+ " Total Files: " + this.getTotalNumFiles() + " Total Bytes: " + this.getTotalNumBytes();
	}
	  	
}
