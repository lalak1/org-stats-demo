package orgstats;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

public class VerySmallDataFileIntegTest {

	@Test
	public void testVerySmallDataSet() {
		
		String dataPath = new java.io.File("").getAbsolutePath() + "//data//";
		OrgStatsTool tool = null;
		
		try {
			tool = new OrgStatsTool(
				new File(dataPath+"VerySmallOrgsTestFile.txt"),
				new File(dataPath+"VerySmallUsersTestFile.txt"),
				new File(dataPath+"VerySmallOutputTestFile.txt"));
			
			OrgCollection orgCollection = tool.getOrgCollection();
			Assert.assertNotNull("Org collection is null.", orgCollection);
			
			// Check Org Bean 1 (top level)
			OrgBean orgBean1 = orgCollection.getOrg(1);
			Assert.assertNotNull("OrgBean for org1 is null", orgBean1);
			Assert.assertEquals("Total org count for getOrg() is incorrect.", 45, orgBean1.getTotalNumUsers());
			Assert.assertEquals("Total file count for getOrg() is incorrect.", 419592, orgBean1.getTotalNumFiles());
			Assert.assertEquals("Total byte count for getOrg() is incorrect.", 238834028484l, orgBean1.getTotalNumBytes());
			
			// Check totals between parent and all children minus parent
			int userCount = 0;
			int fileCount = 0;
			long byteCount = 0;
			List<OrgBean> secondLevelOrgs = orgBean1.getChildOrgs();
			for (OrgBean secondLevelOrg: secondLevelOrgs) {
				userCount = userCount + secondLevelOrg.getTotalNumUsers();
				fileCount = fileCount + secondLevelOrg.getTotalNumFiles();
				byteCount = byteCount + secondLevelOrg.getTotalNumBytes();
			}

			Assert.assertEquals("Total org count w/out parent is incorrect.", orgBean1.getTotalNumUsers()-5, userCount);
			Assert.assertEquals("Total file count w/out parent is incorrect.", orgBean1.getTotalNumFiles()-42103, fileCount);
			Assert.assertEquals("Total byte count w/out parent is incorrect.", orgBean1.getTotalNumBytes()-22292745269l, byteCount);
			
			List<OrgBean> orgsWithoutOrg1 = orgCollection.getOrgTree(1, false);
			Assert.assertEquals("Total org count for getOrgTree(1, false) is incorrect.", 8, orgsWithoutOrg1.size());
			
			List<OrgBean> orgsWithOrg1 = orgCollection.getOrgTree(1, true);
			Assert.assertEquals("Total org count for getOrgTree(1, true) is incorrect.", 9, orgsWithOrg1.size());
			
			
		} catch (Exception ex) {
			Assert.fail("Unexpected Exception " + ex.getMessage());
		}

	}
	 
}
