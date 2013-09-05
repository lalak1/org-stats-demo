package orgstats;

/**
 * Represents a user that is associated to an organization and the users file and byte usage information. 
 */
public class UserBean {

	private int userId;
	private int orgId;
	private int numFiles;
	private long numBytes;
	
	/**
	 * Constructs a UserBean 
	 * 
	 * @param userId		the unique identifier for a UserBean
	 * @param orgId			the organization the user is associated to
	 * @param numFiles		the total number of files for the user
	 * @param numBytes		the total number of bytes for the user
	 */
	public UserBean(int userId, int orgId, int numFiles, long numBytes) {
		this.userId = userId;
		this.orgId = orgId;
		this.numFiles = numFiles;
		this.numBytes = numBytes;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public int getNumFiles() {
		return numFiles;
	}
	public void setNumFiles(int numFiles) {
		this.numFiles = numFiles;
	}
	public long getNumBytes() {
		return numBytes;
	}
	public void setNumBytes(long numBytes) {
		this.numBytes = numBytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
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
		UserBean other = (UserBean) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserBean [userId=" + userId + ", orgId=" + orgId
				+ ", numFiles=" + numFiles + ", numBytes=" + numBytes + "]";
	}
	
	
}
