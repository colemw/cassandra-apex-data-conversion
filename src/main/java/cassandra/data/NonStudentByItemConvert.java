package cassandra.data;

import com.datastax.driver.core.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by colemw on 9/25/14.
 *
 * Class to hold methods for the data conversion and migration from source to target CFs
 *
 */

public class NonStudentByItemConvert implements Convert {

    private String cf;
    private CourseStructureStatementCreator statementCreator;

    public NonStudentByItemConvert (String sourceCf, CourseStructureStatementCreator sc) {

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

        Statement select = statementCreator.createNonStudentByItemSelect(key);
        select.setConsistencyLevel(ConsistencyLevel.QUORUM);
        ResultSet results = sourceSession.execute(select);
        List<DataObject> nonStudentByItem = new ArrayList<>();

        for (Row row : results) {

            nonStudentByItem.add(new NonStudentByItemDAO(row));
        }

        return nonStudentByItem;

    }

    public void setRowsByPartKey(Session targetSession, List<DataObject> inputList) {

        List<DataObject> outputList = new ArrayList<>();

        for (DataObject nstbi : inputList) {

            NonStudentByItemDAO newNstbi = convertRow(nstbi);
            outputList.add(newNstbi);

        }

        List<Statement> inserts = statementCreator.createNonStudentByItemInsert(outputList);
        BatchStatement batch = new BatchStatement();
        batch.setConsistencyLevel(ConsistencyLevel.QUORUM);

        for (Statement insert : inserts) {

            batch.add(insert);
        }

        targetSession.execute(batch);
    }

    // href from existing object gets assignesd to cnthref
    // ctype is assigned the static value from CONTEXT_TYPE_DEFAULT within DataObject

    public NonStudentByItemDAO convertRow(DataObject nstbi) {

        String cnttype = null;
        String cntid = null;
        Date duedate = null;

        NonStudentByItemDAO tempNstbi = ((NonStudentByItemDAO) nstbi);

        return new NonStudentByItemDAO(
                tempNstbi.getCid()
                ,tempNstbi.getCtype()
                ,tempNstbi.getId()
                ,tempNstbi.getDsc()
                ,cnttype
                ,cntid
                ,tempNstbi.getHref()
                ,tempNstbi.getLmd()
                ,tempNstbi.getLvl()
                ,tempNstbi.getRpid()
                ,tempNstbi.getSo()
                ,tempNstbi.getTn()
                ,tempNstbi.getTtl()
                ,duedate
                ,tempNstbi.getLock()
        );

    }


}
