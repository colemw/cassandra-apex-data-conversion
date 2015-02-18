package cassandra.util;

import cassandra.cluster.CassandraData;
import cassandra.data.*;
import com.datastax.driver.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import java.util.*;

/**
 * Created by colemw on 9/25/14.
 *
 * Data migration/conversion method for Apex coursestructure KS
 *
 */

public class DataConverter {

	private static Logger logger = LoggerFactory.getLogger(DataConverter.class);

    public static void main (String[] args) throws Exception {

        // if (args.length < 2)
	    // throw new Exception("Must provide a from host and a to host");

	    ArrayList<String> sourceHostList = new ArrayList<>();

        // sourceHostList.add(args[0]);
        sourceHostList.add("stg-use1c-pr-27-csapub-cadb-c01-0002.prv-openclass.com");
        sourceHostList.add("stg-use1b-pr-27-csapub-cadb-c01-0003.prv-openclass.com");

        ArrayList<String> targetHostList = new ArrayList<>();

        // targetHostList.add(args[1]);
        targetHostList.add("dev-use1b-pr-34-mikecluster-cadb-0001.prv-openclass.com");
        targetHostList.add("dev-use1c-pr-34-mikecluster-cadb-0001.prv-openclass.com");

        String sourceKeySpace = "coursestructure";
        String targetKeySpace = "coursestructure";

        String cfStudentItem = "studentitems";
        String cfNonStudentItems = "nonstudentitems";
        String cfStudentByItem = "studentbyitem";
        String cfNonStudentByItem = "nonstudentbyitem";

        //// Counter for testing output ////
        int keyCounter = 0;

        CassandraData sourceCluster = new CassandraData(sourceHostList);
        CassandraData targetCluster = new CassandraData(targetHostList);

        Session sourceSession = sourceCluster.createSession(sourceKeySpace);
        Session targetSession = targetCluster.createSession(targetKeySpace);

        CourseStructureStatementCreator statementCreator = new CourseStructureStatementCreator(sourceSession, targetSession);

        //// Cluster info output for testing ////

        logger.info("Source Cluster: ");
        logger.info(sourceCluster.getCassandraInfo());
        logger.info("");
        logger.info("Target Cluster: ");
        logger.info(targetCluster.getCassandraInfo());
        logger.info("");
        logger.info("Sending output to dataCVout.txt");
        logger.info("");

        ////// Send output to a file //////

        File out = new File("dataCVout.txt");
        FileOutputStream outputStream = new FileOutputStream(out);
        PrintStream ps = new PrintStream(outputStream);

        ///////////////////////////////////

        // Convert StudentItems
        // Pull all data from each existing CF

        StudentItemsConvert siConvert = new StudentItemsConvert(cfStudentItem, statementCreator);
        ResultSet siKeys = siConvert.getKeys(sourceSession);

        // Loop through partition keys of each CF
        // Retrieve all Rows from each key and save into a List<>

        // System.setOut(ps);
        logger.info("Starting CF " + cfStudentItem + ":");
        logger.info("");

        for (Row key : siKeys) {

            keyCounter = keyCounter + 1;

            String partitionKey = key.getString("cid");

            // Get all rows for each partition key
            List<DataObject> siRows = siConvert.getRowsByPartKey(partitionKey,sourceSession);

            // Convert the data in the list by creating new data objects and insert into the new cluster
            siConvert.setRowsByPartKey(targetSession,siRows);

            //// Keys completed for testing
            logger.info("Partition key " + partitionKey + " complete." );
            logger.info("");
            logger.info("Total partitions complete: " + Integer.toString(keyCounter));
            logger.info("");

        }

        // Convert StudentBytItem
        // Pull all data from each existing CF

        StudentByItemConvert sbiConvert = new StudentByItemConvert(cfStudentByItem, statementCreator);
        ResultSet sbiKeys = sbiConvert.getKeys(sourceSession);

        for (Row key : sbiKeys) {

            keyCounter = keyCounter + 1;

            String partitionKey = key.getString("cid");

            // Get all rows for each partition key
            List<DataObject> sbiRows = sbiConvert.getRowsByPartKey(partitionKey,sourceSession);

            // Convert the data in the list by creating new data objects and insert into the new cluster
            sbiConvert.setRowsByPartKey(targetSession,sbiRows);

            //// Keys completed for testing
            logger.info("Partition key " + partitionKey + " complete." );
            logger.info("");
            logger.info("Total partitions complete: " + Integer.toString(keyCounter));
            logger.info("");

        }

        // Convert NonStudentItems
        // Pull all data from each existing CF

        NonStudentItemsConvert nsiConvert = new NonStudentItemsConvert(cfNonStudentItems, statementCreator);
        ResultSet nsiKeys = nsiConvert.getKeys(sourceSession);

        for (Row key : nsiKeys) {

            keyCounter = keyCounter + 1;

            String partitionKey = key.getString("cid");

            // Get all rows for each partition key
            List<DataObject> nsiRows = nsiConvert.getRowsByPartKey(partitionKey,sourceSession);

            // Convert the data in the list by creating new data objects and insert into the new cluster
            nsiConvert.setRowsByPartKey(targetSession,nsiRows);

            //// Keys completed for testing
            logger.info("Partition key " + partitionKey + " complete." );
            logger.info("");
            logger.info("Total partitions complete: " + Integer.toString(keyCounter));
            logger.info("");

        }

        // Convert NonStudentByItems
        // Pull all data from each existing CF

        NonStudentByItemConvert nsbiConvert = new NonStudentByItemConvert(cfNonStudentByItem, statementCreator);
        ResultSet nsbiKeys = nsbiConvert.getKeys(sourceSession);

        for (Row key : nsbiKeys) {

            keyCounter = keyCounter + 1;

            String partitionKey = key.getString("cid");

            // Get all rows for each partition key
            List<DataObject> nsbiRows = nsbiConvert.getRowsByPartKey(partitionKey,sourceSession);

            // Convert the data in the list by creating new data objects and insert into the new cluster
            nsbiConvert.setRowsByPartKey(targetSession,nsbiRows);

            //// Keys completed for testing
            logger.info("Partition key " + partitionKey + " complete." );
            logger.info("");
            logger.info("Total partitions complete: " + Integer.toString(keyCounter));
            logger.info("");

        }

        sourceSession.close();
        targetSession.close();

        System.exit(1);
    }

}
