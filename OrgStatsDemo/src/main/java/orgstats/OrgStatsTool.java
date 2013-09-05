package orgstats;

import static orgstats.OrgStatsHelper.isInteger;

import java.io.File;
import java.util.Scanner;

import orgstats.load.OrgDataLoader;
import orgstats.load.OrgDataLoaderException;
import orgstats.load.OrgUserDataFileLoader;
import orgstats.store.OrgStatsFileStore;
import orgstats.store.OrgStatsStore;
import orgstats.store.OrgStatsStoreException;

/**
 * Tool for reading hierarchical organizational and related user file and byte usage data
 * into memory in order to produce usage statistics on demand.  After reading the 
 * organizational and user data, it will be written to a third file, which represents
 * the organizational tree. 
 * <p>
 * NOTE:  Files 
 * <p>
 * A very simple user interface will also provide the ability to view the usage statistics
 * for an single organization or a tree of organizations.
 * <p>
 */
public class OrgStatsTool {
	
	private OrgCollection orgCollection;
	private OrgDataLoader orgDataLoader;
	private OrgStatsStore orgStatsStore;
	
	/**
	 * Constructs an OrgStats Tool for the input and output files.
	 * Data is loaded into memory and the output file is written.
	 * 
	 * @param orgFile		the File that contains the org data
	 * @param userFile		the File that contains the user data
	 * @param outputFile	the File output file to be created after processing
	 */
	public OrgStatsTool(File orgFile, File userFile, File outputFile) throws OrgDataLoaderException, OrgStatsStoreException {
			
		orgCollection = new OrgCollection();
		orgDataLoader = new OrgUserDataFileLoader(orgFile, userFile);
		orgStatsStore = new OrgStatsFileStore(outputFile);
		initializeData();
	}
	
	/**
	 * Runs the OrgStatsTool given the input and output filesname as arguments
	 * 
	 * @param args[0]		the name of the file containing the org data
	 * @param args[1]		the name of the file containing the user data
	 * @param args[2]		the name of the output data file
	 */
	public static void main(String[] args) {

		if (args.length == 3) {
			File orgFile = new File(args[0]);
			File userFile = new File(args[1]);
			File outputFile = new File(args[2]);			
			
			try {
				OrgStatsTool orgStatsTool = new OrgStatsTool(orgFile, userFile, outputFile);
				orgStatsTool.handleStatRequests();
			} catch (OrgDataLoaderException loaderEx) {
				System.err.println("OrgStatsTool stopped on loading data..." + loaderEx.getMessage());
			} catch (OrgStatsStoreException storeEx) {
				System.err.println("OrgStatsTool stopped on storing data..." + storeEx.getMessage());
				
			}
		}
	}
	
	/**
	 * Returns the organization tree of data stored in memory.
	 * 
	 * @return OrgCollection		a container for OrgBeans
	 */
	public OrgCollection getOrgCollection() {
		return orgCollection;
	}

	/*
	 * Initialize the data by reading it into memory and writing it to an output file.
	 */
	void initializeData() throws OrgDataLoaderException, OrgStatsStoreException {

		// Load organization and user data in memory
		orgCollection = orgDataLoader.loadData();
		
		// Store organization statistic results in output file
		orgStatsStore.storeData(orgCollection);
	}
	
	/*
	 * Simple user interface for displaying the org usage data.
	 * <p>
	 * <ul>
	 * 	<li>[1] Get Individual Org Stats
	 *  <li>[2] Get Org Tree Stats (Exclusive)
	 *  <li>[3] Get Org Tree Stats (Inclusive)
	 * </ul>
	 */
	private void handleStatRequests() {
		
		Scanner scanner = new Scanner(System.in);
		while (true) {
			printMenu();
		    String input = scanner.next();
		    if (input.equalsIgnoreCase("q")) {
		        break;
		    } else {
		    	if ("1".equals(input) || "2".equals(input) || "3".equals(input)) {
		    		System.out.println("Enter OrgId:");
			    	String orgIdInput = scanner.next();
			    	if (isInteger(orgIdInput)) { 
				    	Integer orgId = Integer.valueOf(orgIdInput);
				    	if ("1".equals(input)) {
				    		System.out.println(orgCollection.getOrg(orgId).getStatsString());
				    	} else if ("2".equals(input)) {
				    		for (OrgBean org : orgCollection.getOrgTree(orgId, false)) {
				    			System.out.println(org.getStatsString());  
				    		}
				    	} else if ("3".equals(input)) {				    	
				    		for (OrgBean org : orgCollection.getOrgTree(orgId, true)) {
				    			System.out.println(org.getStatsString());  
				    		}
				    	}
			    	} else {
			    		System.out.println("Invalid menu selection.\n");
			    	}
		    	} else {
		    		System.out.println("Invalid org id.\n");
		    	}
		    }
		}
	}
	
	/*
	 * Prints simple menu
	 */
	private void printMenu() {
		System.out.println("\nOrgCollection Stats->Enter a selection:");
		System.out.println("[1] Get Individual Org Stats");
		System.out.println("[2] Get Org Tree Stats (Exclusive)");
		System.out.println("[3] Get Org Tree Stats (Inclusive)");
		System.out.println("[q] Quit");
	}
}
