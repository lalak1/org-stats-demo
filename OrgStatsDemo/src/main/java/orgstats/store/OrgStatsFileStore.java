package orgstats.store;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import orgstats.OrgBean;
import orgstats.OrgCollection;

/**
 * Data store for organizational hierarchy usage statistics using a File.
 */
public class OrgStatsFileStore implements OrgStatsStore {

	private static final Logger logger = Logger.getLogger(OrgStatsFileStore.class.getName());
	private static final String INDENT = "  ";
	private File outputFile;
	
	/**
	 * Constructs OrgStatsFileStore with the File to be used for output
	 * 
	 * @param outputFile	the File used to write the org summary data
	 */
	public OrgStatsFileStore(File outputFile) throws OrgStatsStoreException {	
		
		try {
			if ((outputFile.exists() && !outputFile.canWrite())
					|| (!outputFile.exists() && !outputFile.createNewFile())) {
				String errMsg = "Output file specified is not valid.";
				logger.log(Level.SEVERE, errMsg);
				throw new OrgStatsStoreException(errMsg);
			}
		} catch (IOException ioEx) {
			String errMsg = "ERROR creating output data file (I/O error)";
			logger.log(Level.SEVERE, errMsg, ioEx);
			throw new OrgStatsStoreException(errMsg, ioEx);	
		} 
	
		this.outputFile = outputFile;
	}

	/**
	 * Stores organization, user, and file usage data from memory into a file
	 * 
	 * @return orgCollection		the OrgCollection containing a tree of OrgBeans			
	 */
	@Override
	public void storeData(OrgCollection orgCollection) throws OrgStatsStoreException {
		
		BufferedWriter outputWriter = null;
		
		try {
			logger.log(Level.INFO, "Writing org tree summary data...");
			outputWriter = new BufferedWriter(new FileWriter(outputFile));
			outputOrgData(outputWriter, orgCollection.getTopLevelOrgs(), 0);
		} catch (IOException ioEx) {
			String errMsg = "ERROR writing data file (I/O error)";
			logger.log(Level.SEVERE, errMsg, ioEx);
			throw new OrgStatsStoreException(errMsg, ioEx);	
		} finally {
			try {
				if (outputWriter != null) {
					outputWriter.close();
				}
			} catch (IOException ioEx) {
				logger.log(Level.WARNING, "Error storing data file", ioEx);
			}
		}
	}
	
	/**
	 * Write the orgBean at the appropriate level for a particular bean.
	 * 
	 * @param outputWriter		the BufferedWriter to output character data to
	 * @param orgBean			the OrgBean that will be written on the current line
	 * @param level				the indentation level of the OrgBean in relation to it's parent
	 * @throws IOException		if any unexpected I/O error occurs
	 */
	void outputOrgData(BufferedWriter outputWriter, OrgBean orgBean, int level) throws IOException {
		outputWriter.append(getIndent(level) + getOutputString(orgBean));
		outputWriter.newLine();
	}
	
	/**
	 * Write the list of OrgBeans and recursively the child beans at the appropriate level for a particular bean.
	 * 
	 * @param outputWriter		the BufferedWriter to output character data to
	 * @param orgBeans			the OrgBeans that will be written on subsequent current line
	 * @param level				the indentation level of the OrgBean in relation to it's parent
	 * @throws IOException		if any unexpected I/O error occurs
	 */
	void outputOrgData(BufferedWriter outputWriter, List<OrgBean> orgBeans, int level) throws IOException {
		
		Collections.sort(orgBeans);
		for (OrgBean orgBean : orgBeans) {
			if (orgBeans.indexOf(orgBean) == 0) {
				level = level + 1;
			}
			outputOrgData(outputWriter, orgBean, level);
			
			outputOrgData(outputWriter, orgBean.getChildOrgs(), level);
		}
	}
	
	/*
	 * Returns the child indentation for the file.
	 */
	private static String getIndent(int level) {
		StringBuilder indent = new StringBuilder(level*INDENT.length());
		for (int i = 0; i < level-1; i++) {
			indent.append(INDENT);
		}
		return indent.toString();
	}
	

	/*
	 * Constructs the output string for each org.
	 */
	public static String getOutputString(OrgBean orgBean) {
		return orgBean.getOrgId() + ", " + orgBean.getTotalNumUsers() + ", " + orgBean.getTotalNumFiles()
				+ ", " + orgBean.getTotalNumBytes();
	}

}
