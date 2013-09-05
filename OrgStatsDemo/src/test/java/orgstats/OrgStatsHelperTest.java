package orgstats;

import junit.framework.Assert;
import static orgstats.OrgStatsHelper.isInteger;
import static orgstats.OrgStatsHelper.containsLetterOrDigit;
import org.junit.Test;


public class OrgStatsHelperTest {
	
	@Test
	public void testIsInteger() {
		
		Assert.assertTrue(isInteger("0"));
		Assert.assertTrue(isInteger("121"));
		Assert.assertTrue(isInteger("12312321342421666"));
		Assert.assertFalse(isInteger(null));
		Assert.assertFalse(isInteger("21231s2321342421"));
		Assert.assertFalse(isInteger("noitisn't"));			
	}
	
	@Test
	public void testContainsLetterOrDigit() {
		
		Assert.assertFalse(containsLetterOrDigit(null));
		Assert.assertFalse(containsLetterOrDigit("\n."));
		Assert.assertFalse(containsLetterOrDigit(""));
		Assert.assertTrue(containsLetterOrDigit("2"));
		Assert.assertTrue(containsLetterOrDigit("a 2"));
		Assert.assertTrue(containsLetterOrDigit("aa"));
	}

}
