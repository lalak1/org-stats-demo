package orgstats;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;

public class SmallDataFileIntegTest {

	@Test
	public void testSmallDataFile() {
		
		String dataPath = new java.io.File("").getAbsolutePath() + "//data//";
		OrgStatsTool tool = null;
		
		try {
			tool = new OrgStatsTool(
				new File(dataPath+"SmallOrgsTestFile.txt"),
				new File(dataPath+"SmallUsersTestFile.txt"),
				new File(dataPath+"SmallOutputTestFile.txt"));
			
			OrgCollection orgCollection = tool.getOrgCollection();
			Assert.assertNotNull("Org collection is null.", orgCollection);
			OrgBean orgBean3 = orgCollection.getOrg(3);
			Assert.assertNotNull("Org 3 is null.  ", orgBean3);
			Assert.assertEquals("Org3 total user count invalid.", 80, orgBean3.getTotalNumUsers());
			Assert.assertEquals("Org3 total user count invalid.", 809976, orgBean3.getTotalNumFiles());
			Assert.assertEquals("Org3 total user count invalid.", 427587370095l, orgBean3.getTotalNumBytes());
			
		} catch (Exception ex) {
			Assert.fail("Unexpected Exception " + ex.getMessage());
		}

	}
	 
}
