package cassandra.data;

import com.datastax.driver.core.*;

import java.util.*;

/**
 * Created by colemw on 9/25/14.
 *
 * Class to hold methods for the data conversion and migration from source to target CFs
 *
 */

public class NonStudentItemsConvert implements Convert {

    private String cf;
    private CourseStructureStatementCreator statementCreator;

    public NonStudentItemsConvert (String sourceCf, CourseStructureStatementCreator sc) {

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

        Statement select = statementCreator.createNonStudentItemsSelect(key);
        select.setConsistencyLevel(ConsistencyLevel.QUORUM);
        ResultSet results = sourceSession.execute(select);
        List<DataObject> nonStudentItems = new ArrayList<>();

        // Get the related data
        Statement itemsSelect = statementCreator.createNonStudentByItemSelect(key);
        List<Row> itemResultRaw = sourceSession.execute(itemsSelect).all();
        Map<UUID, NonStudentByItemDAO> byItems = new HashMap<>();

        for (Row item : itemResultRaw) {
            NonStudentByItemDAO newItem = new NonStudentByItemDAO(item);
            byItems.put(newItem.getId(), newItem);
        }

        for (Row row : results) {
            NonStudentItemsDAO newItem = new NonStudentItemsDAO(row);

            if (byItems.containsKey(newItem.getId()))
                newItem.setDsc(byItems.get(newItem.getId()).getDsc());

            nonStudentItems.add(newItem);
        }

        return nonStudentItems;

    }

    public void setRowsByPartKey(Session targetSession, List<DataObject> inputList) {

        List<DataObject> outputList = new ArrayList<>();

        for (DataObject nsti : inputList) {

            NonStudentItemsDAO newNsti = convertRow(nsti);
            outputList.add(newNsti);

        }

        List<Statement> inserts = statementCreator.createNonStudentItemsInsert(outputList);
        BatchStatement batch = new BatchStatement();
        batch.setConsistencyLevel(ConsistencyLevel.QUORUM);

        for (Statement insert : inserts) {

            batch.add(insert);
        }

        targetSession.execute(batch);
    }

    // href from existing object gets assignesd to cnthref
    // ctype is assigned the static value from CONTEXT_TYPE_DEFAULT within DataObject

    public NonStudentItemsDAO convertRow(DataObject nsti) {

        String cnttype = null;
        String cntid = null;
        Date duedate = null;

        NonStudentItemsDAO tempNsti = ((NonStudentItemsDAO) nsti);

        return new NonStudentItemsDAO(
                tempNsti.getCid()
                ,tempNsti.getCtype()
                ,tempNsti.getSo()
                ,tempNsti.getId()
                ,tempNsti.getDsc()
                ,cnttype
                ,cntid
                ,tempNsti.getHref()
                ,tempNsti.getLmd()
                ,tempNsti.getLvl()
                ,tempNsti.getRpid()
                ,tempNsti.getTn()
                ,tempNsti.getTtl()
                ,duedate
                ,tempNsti.getLock()
        );

    }
}
