package cassandra.data;


import com.datastax.driver.core.*;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.BatchStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by colemw on 9/25/14.
 *
 * Class to hold methods for the data conversion and migration from source to target CFs
 *
 */

public class StudentItemsConvert implements Convert {

    private String cf;
    private CourseStructureStatementCreator statementCreator;

    public StudentItemsConvert (String sourceCf, CourseStructureStatementCreator sc) {

        cf = sourceCf;
        statementCreator = sc;

    }

    public ResultSet getKeys(Session session) {

        Statement stmt = new SimpleStatement("SELECT DISTINCT cid  FROM " + cf + " ALLOW FILTERING");
        stmt.setFetchSize(25000);
        stmt.setConsistencyLevel(ConsistencyLevel.QUORUM);

        return session.execute(stmt);

    }

    public List<DataObject> getRowsByPartKey(String key, Session sourceSession) {

	    // Get the data to convert from the items table
        Statement select = statementCreator.createStudentItemsSelect(key);
        select.setConsistencyLevel(ConsistencyLevel.QUORUM);
        ResultSet results = sourceSession.execute(select);
        List<DataObject> studentItems = new ArrayList<>();

	    // Get its related data from the byitems table
	    Statement itemsSelect = statementCreator.createStudentByItemSelect(key);
	    List<Row> itemResultRaw = sourceSession.execute(itemsSelect).all();
        Map<UUID, StudentByItemDAO> byItems = new HashMap<>();

        for (Row item : itemResultRaw) {
            StudentByItemDAO newItem = new StudentByItemDAO(item);
            byItems.put(newItem.getId(), newItem);
        }

        for (Row row : results) {
            StudentItemsDAO newStudentItem = new StudentItemsDAO(row);

            if (byItems.containsKey(newStudentItem.getId()))
                newStudentItem.setDsc(byItems.get(newStudentItem.getId()).getDsc());

            studentItems.add(newStudentItem);
        }

        return studentItems;

    }

    public void setRowsByPartKey(Session targetSession, List<DataObject> inputList) {

        List<DataObject> outputList = new ArrayList<>();

        for (DataObject sti : inputList) {

            StudentItemsDAO newSti = convertRow(sti);
            outputList.add(newSti);

        }

        List<Statement> inserts = statementCreator.createStudentItemsInsert(outputList);
        BatchStatement batch = new BatchStatement();
        batch.setConsistencyLevel(ConsistencyLevel.QUORUM);

        for (Statement insert : inserts) {

            batch.add(insert);
        }

        targetSession.execute(batch);
    }

    // href from existing object gets assignesd to cnthref
    // ctype is assigned the static value from CONTEXT_TYPE_DEFAULT within DataObject

    public StudentItemsDAO convertRow(DataObject sti) {

        String cnttype = null;
        String cntid = null;
        Date duedate = null;

        StudentItemsDAO tempSti = ((StudentItemsDAO) sti);

        return new StudentItemsDAO(
                tempSti.getCid()
                ,tempSti.getCtype()
                ,tempSti.getSo()
                ,tempSti.getId()
                ,tempSti.getDsc()
                ,cnttype
                ,cntid
                ,tempSti.getHref()
                ,tempSti.getLmd()
                ,tempSti.getLvl()
                ,tempSti.getRpid()
                ,tempSti.getTn()
                ,tempSti.getTtl()
                ,duedate
        );

    }
}
