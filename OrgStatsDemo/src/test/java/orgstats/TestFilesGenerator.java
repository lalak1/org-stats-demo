package orgstats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Creates test organization hierarchy and user data files for test purposes.
 */
public class TestFilesGenerator {
	
	private static final Logger logger = Logger.getLogger(TestFilesGenerator.class.getName());
	private static final String SEPARATOR = ", ";
	
													// ~500 million lines
	private static boolean random = true; 			// false
	private static int orgCount = 	3; 				// 1000
	private static int maxOrgChildCount = 3; 		// 5
	private static int maxUserCount = 5; 			// 50
	private static int maxOrgLevels = 3;			// 5
	private static int maxFileCount = 20000;		
	private static long maxByteCount = 10737418240l;
	
	private static long userIdCounter = 1;
	private static int orgIdCounter = 1;
	private static Random randomGenerator = new Random();	
	private static BufferedWriter orgWriter = null;
	private static BufferedWriter userWriter = null;

	private static String dataPath = new java.io.File("").getAbsolutePath() + "//data//";
	
	/**
	 * Creates two test files entitled OrgsTestFile.txt and UsersTestFile.txt 
	 * that are in the <absolutepath>/data directory.
	 */
	public static void main(String args[]) {
				
		//generateData("OrgsTestFile.txt", "UsersTestFile.txt");
		generateData("OrgsTestFile.txt", "UsersTestFile.txt", false, 1000,
				5, 50, 5, 20000, 10737418240l);
	}
	
	/**
	 * Generates data with the processing-related values specified. 
	 * 
	 * @param randomVal				whether or not the "max" values are used or if they are random
	 * @param orgCountVal			top level org count
	 * @param maxOrgChildCountVal	maximum number of org children for each org (0 to max unless random)
	 * @param maxUserCountVal		maximum number of users for each org (0 to max unless random)
	 * @param maxOrgLevelsVal		maximum number of org levels (0 to max unless random)
	 * @param maxFileCountVal		maximum number of files for each user (0 to max unless random)
	 * @param maxByteCountVal		maximum number of bytes (0 to max unless random)
	 */
	static void generateData(String orgDataFilename, String userDataFilename, boolean randomVal, 
			int orgCountVal, int maxOrgChildCountVal, int maxUserCountVal, 
			int maxOrgLevelsVal, int maxFileCountVal, long maxByteCountVal) {
		random = randomVal;
		orgCount = orgCountVal;
		maxOrgChildCount = maxOrgChildCountVal;
		maxUserCount = maxUserCountVal;
		maxOrgLevels = maxOrgLevelsVal;
		maxFileCount = maxFileCountVal;
		maxByteCount = maxByteCountVal;
		
		generateData(orgDataFilename, userDataFilename);
		
	}
	
	/**
	 * Generates an org data file and a user data file with a random data using default data generating values.
	 * <p>
	 * Data files are created in a directory entitled "data" under the absolute path.  
	 * Existing files will be overwritten.  
	 */
	static void generateData(String orgDataFilename, String userDataFilename) {
		
		Date startTime = new Date();
		try {
			//System.err.println(System.getProperty("java.io.tmpdir"));
			orgWriter = new BufferedWriter(new FileWriter(new File(dataPath+orgDataFilename)));			
			userWriter = new BufferedWriter(new FileWriter(new File(dataPath+userDataFilename)));

			for (int o=1; o <= orgCount; o++) { 
				writeData(orgIdCounter, 0, 0);
				orgIdCounter = orgIdCounter +1;
			}	
			
		} catch (IOException ioEx) {
			logger.log(Level.SEVERE, "Unable to generate test data.", ioEx);
		} finally {
			try {
				if (orgWriter != null) {
					orgWriter.close();
				}
			} catch (IOException ioEx) {
				// Nothing to do here.
			}
			try {
				if (userWriter != null) {
					userWriter.close();
				}
			} catch (IOException ioEx) {
				// Nothing to do here.
			}
		}
		
		Date endTime = new Date();
		logger.log(Level.INFO, "DATA GENERATED:  " + (endTime.getTime() - startTime.getTime()) 
				+"ms to process " + orgIdCounter + " orgs " + userIdCounter + " users ");
	}
	
	static void writeData(Integer orgId, Integer parentOrgId, Integer level) 
			throws IOException {

		writeToOrgFile(orgIdCounter, parentOrgId);
		
		int userCount = random ? maxUserCount : randomGenerator.nextInt(maxUserCount+1);	
		for (int u=1; u <= userCount ; u++) {
			int fileCount = random ? randomGenerator.nextInt(maxFileCount+1) : maxFileCount;
			long byteCount = random ? (long)(randomGenerator.nextDouble()*(maxByteCount+1)) : maxByteCount;
			writeToUserFile(userIdCounter, orgId, fileCount, byteCount);
			userIdCounter = userIdCounter + 1;
		}
		

		int childCount = 0;
		if (level <= maxOrgLevels) { 
			childCount = random ? randomGenerator.nextInt(maxOrgChildCount+1) : maxOrgChildCount;	
		} 

		for (int c=1; c <= childCount; c++) {
			if (c==1) {
				parentOrgId = orgId;
				level = level +1;
			} 
			orgIdCounter = orgIdCounter + 1;
			writeData(orgIdCounter, parentOrgId, level);
		}		
	}
	
	private static void writeToOrgFile(int orgId, int parentOrgId) throws IOException {

		orgWriter.append(orgIdCounter + SEPARATOR + (parentOrgId == 0 ? "null" : parentOrgId) + SEPARATOR 
					+ "Org" + String.valueOf(orgId));
		orgWriter.newLine();
	}
	
	private static void writeToUserFile(long userIdCounter, int orgId, int fileCount, 
			long byteCount) throws IOException {
		
		userWriter.append(userIdCounter + SEPARATOR + orgId + SEPARATOR + fileCount + SEPARATOR + byteCount);
		userWriter.newLine();
	}
	
}
