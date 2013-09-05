package orgstats;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultIntegTest {

	private static final String DATA_PATH = new java.io.File("").getAbsolutePath() + "//data//";
	private static final long timestamp = System.currentTimeMillis();
	private static final String ORG_FILENAME = "OrgsTestFile.txt";
	private static final String USER_FILENAME = "UsersTestFile.txt";
	private static final String OUTPUT_FILENAME = DATA_PATH + "OutputTestfile.txt";
	
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
