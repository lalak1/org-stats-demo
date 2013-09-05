package orgstats;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * A general integration test that creates test files and then processes them.
 * 
 * The files will be stored in the "data" directory on the absolute path and will
 * follow the format:
 * <p>
 * <ul>
 * 	<li>Orgs Data File: GenericOrgsData<currentTimeInMillis>.txt</li>
 *  <li>Users Data File: GenericUsersData<currentTimeInMillis>.txt</li>
 *  <li>Ouput File: GenericOrgsData<currentTimeInMillis>.txt</li>
 * </ul>
 */
public class GenericIntegTest {

	private static final String DATA_PATH = new java.io.File("").getAbsolutePath() + "//data//";
	private static final long timestamp = System.currentTimeMillis();
	private static final String ORG_FILENAME = "GenericOrgsData" + timestamp + ".txt";
	private static final String USER_FILENAME = "GenericUsersData" + timestamp + ".txt";
	private static final String OUTPUT_FILENAME = DATA_PATH + "GenericOutput" + timestamp + ".txt";
	
	/*
	 * Generate Files
	 */
	@Before
	public void setup() {
		
		TestFilesGenerator.generateData(ORG_FILENAME, USER_FILENAME);
	}
	
	@Test
	public void testGenericIntegTest() {
		
		OrgStatsTool tool = null;
		
		System.err.println(ORG_FILENAME);
		try {tool = new OrgStatsTool(
				new File(DATA_PATH + ORG_FILENAME),
				new File(DATA_PATH + USER_FILENAME),
				new File(OUTPUT_FILENAME));
			
			OrgCollection orgCollection = tool.getOrgCollection();
			Assert.assertNotNull("Org collection is null.", orgCollection);
			
			// Check Org Bean 1 (top level)
			OrgBean orgBean1 = orgCollection.getOrg(1);
			Assert.assertNotNull("OrgBean for org1 is null", orgBean1);
			
			List<OrgBean> orgsWithoutOrg1 = orgCollection.getOrgTree(1, false);
			Assert.assertTrue("Total org count for getOrgTree(1, false) needs to be greater than 1", orgsWithoutOrg1.size() > 1);
					
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unexpected Exception " + ex.getMessage());
		}
		
	}
	
	
}
