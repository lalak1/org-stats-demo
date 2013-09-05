package orgstats.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import orgstats.OrgBean;
import orgstats.OrgCollection;
import orgstats.UserBean;

import static orgstats.OrgStatsHelper.containsLetterOrDigit;
import static orgstats.OrgStatsHelper.isInteger;

/**
 * Loader for creating a collection of organizations from an org data file
 * and a use data file.
 */
public class OrgUserDataFileLoader implements OrgDataLoader {
	
	private static final Logger logger = Logger.getLogger(OrgUserDataFileLoader.class.getName());
	private static final String SEPARATOR = ", ";
	private static final String NULL_STRING = "null"; 
	
	private File orgFile;
	private File userFile;
	
	/**
	 * Constructs a new OrgDataLoader given a org data File and user data File. 
	 * 
	 * @param orgFile
	 * @param userFile
	 * 
	 * @throws OrgDataLoaderException   Thrown if either the orgFile or userFile do not exist.
	 */
	public OrgUserDataFileLoader(File orgFile, File userFile) throws OrgDataLoaderException {
		
		if (!orgFile.exists() || !orgFile.canRead()) {
			String errMsg = "Organization file specified is not valid.";
			logger.log(Level.SEVERE, errMsg);
			throw new OrgDataLoaderException(errMsg);
		}
		
		if (!userFile.exists() || !userFile.canRead()) {
			String errMsg = "User file specified is not valid.";
			logger.log(Level.SEVERE, errMsg);
			throw new OrgDataLoaderException(errMsg);
		}
	
		this.orgFile = orgFile;
		this.userFile = userFile;
	}

	/**
	 * Populates organization, user, and file usage data into an OrgCollection from two
	 * separate input files.
	 * 
	 * @return orgCollection		the OrgCollection containing a tree of OrgBeans			
	 */
	@Override
	public OrgCollection loadData() throws OrgDataLoaderException {

		OrgCollection orgCollection = new OrgCollection();
		populateOrgs(orgCollection);
		populateUsers(orgCollection);
		return orgCollection;
	}
	
	/**
	 * Populates the orgCollection with OrgBeans constructed from each line in the data file.
	 *  
	 * @param orgCollection		the OrgCollection to be populated with OrgBeans
	 * @throws OrgDataLoaderException	thrown if there are not three data items separated by commas,
	 *                          the orgId, and parentOrgId aren't integers, or the orgName doesn't
	 *                          contain a number or letter
	 */
	void populateOrgs(OrgCollection orgCollection) throws OrgDataLoaderException {
		
		BufferedReader orgReader = null;
		try {
			logger.log(Level.INFO, "Reading org hierarchy data file...");
			orgReader = new BufferedReader(new FileReader(orgFile));
			String currentLine;
			while ((currentLine = orgReader.readLine()) != null) {
				orgCollection.add(constructOrgBean(currentLine));
			}
			
		} catch (IOException ioEx) {
			String errMsg = "ERROR processing org data file (I/O error)";
			logger.log(Level.SEVERE, errMsg, ioEx);
			throw new OrgDataLoaderException(errMsg, ioEx);	
		} finally {
			try {
				if (orgReader != null) {
					orgReader.close();
				}
			} catch (IOException ioEx) {
				logger.log(Level.WARNING, "Error closing org data file", ioEx);
			}
		}
	}
	
	/**
	 * Populates the orgCollection with UserBean objects constructed from each line in the data file.
	 * The UserBean objects are put in the collection on the appropriate OrgBean.
	 * 
	 * @param orgCollection 			the OrgCollection to be populated with UserBeans
	 * @throws OrgDataLoaderException
	 */
	void populateUsers(OrgCollection orgCollection) throws OrgDataLoaderException {
		
		BufferedReader userReader = null;
		try {
			logger.log(Level.INFO, "Reading user hierarchy data file...");
			userReader = new BufferedReader(new FileReader(userFile));
			String currentLine;
			while ((currentLine = userReader.readLine()) != null) {
				orgCollection.addUser(constructUserBean(currentLine));
			}
			
		} catch (IOException ioEx) {
			String errMsg = "ERROR processing user data file (I/O error)";
			logger.log(Level.SEVERE, errMsg, ioEx);
			throw new OrgDataLoaderException(errMsg, ioEx);	
		} finally {
			try {
				if (userReader != null) {
					userReader.close();
				}
			} catch (IOException ioEx) {
				logger.log(Level.WARNING, "Error closing user data file", ioEx);
			}
		}
	}
	
	/**
	 * Constructs an OrgBean given a line of data with this format:
	 * <p>
	 *  orgId, parentOrgId, orgName
	 * 
	 * @param currentLine		the line containing the orgId, parentOrgId, and orgName
	 * @return OrgBean			the OrgBean constructed using the currentLine data
	 * @throws OrgDataLoaderException	thrown if there are not three data items separated by commas,
	 *                          the orgId, and parentOrgId aren't integers, or the orgName doesn't
	 *                          contain a number or letter
	 */
	OrgBean constructOrgBean(String currentLine) throws OrgDataLoaderException {
		
		String orgData[] = currentLine.split(SEPARATOR);
		if (orgData.length != 3) {
			throw new OrgDataLoaderException("ERROR with Org File (not 3 items on line): "+ currentLine);
		}
		if (!isInteger(orgData[0]))  {
			throw new OrgDataLoaderException("ERROR with Org File (invalid org id): "+ currentLine);
		}
		if (!isInteger(orgData[1]) && !NULL_STRING.endsWith(orgData[1])) {
			throw new OrgDataLoaderException("ERROR with Org File (invalid parent org id): "+ currentLine);
		}
		if (!containsLetterOrDigit(orgData[2])) {
			throw new OrgDataLoaderException("ERROR with Org File (invalid org name): "+ currentLine);
		}
		Integer orgId = Integer.valueOf(orgData[0]);
		Integer parentOrgId = NULL_STRING.equals(orgData[1]) ? 0 : Integer.valueOf(orgData[1]);
		String orgName = orgData[2];
		
		return new OrgBean(orgId, parentOrgId, orgName);
	}

	/**
	 * Constructs an UserBean given a line of data with this format:
	 * <p>
	 *  userId, orgId, fileCount, byteCount
	 * 
	 * @param currentLine		the line containing the userId, orgId, fileCount, byteCount
	 * @return OrgBean			the UserBean constructed using the currentLine data
	 * @throws OrgDataLoaderException	thrown if there are not four data items separated by commas,
	 *                          the userId, orgId, fileCount, and/or byteCount aren't numbers
	 */
	UserBean constructUserBean(String currentLine) throws OrgDataLoaderException {
		
		String userData[] = currentLine.split(SEPARATOR);
		if (userData.length != 4) {
			throw new OrgDataLoaderException("ERROR with User File (not 4 items on line): "+ currentLine);
		}
		if (!isInteger(userData[0])) {
			throw new OrgDataLoaderException("ERROR with User File (invalid user id): "+ currentLine);
		}
		if (!isInteger(userData[1])) {
			throw new OrgDataLoaderException("ERROR with User File (invalid org id): "+ currentLine);
		}
		if (!isInteger(userData[2])) {
			throw new OrgDataLoaderException("ERROR with User File (invalid fileCount): "+ currentLine);
		}
		if (!isInteger(userData[3])) {
			throw new OrgDataLoaderException("ERROR with User File (invalid fileByte): "+ currentLine);
		}

		Integer userId = Integer.valueOf(userData[0]);
		Integer orgId = Integer.valueOf(userData[1]);
		Integer fileCount = Integer.valueOf(userData[2]);
		Long byteCount = Long.valueOf(userData[3]);
		
		return new UserBean(userId, orgId, fileCount, byteCount);
	}
}
