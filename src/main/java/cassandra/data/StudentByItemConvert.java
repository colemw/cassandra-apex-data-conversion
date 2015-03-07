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

public class StudentByItemConvert implements Convert {

    private String cf;
    private CourseStructureStatementCreator statementCreator;

    public StudentByItemConvert (String sourceCf, CourseStructureStatementCreator sc) {

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

        Statement select = statementCreator.createStudentByItemSelect(key);
        select.setConsistencyLevel(ConsistencyLevel.QUORUM);
        ResultSet results = sourceSession.execute(select);
        List<DataObject> studenBytItems = new ArrayList<>();

        for (Row row : results) {

            studenBytItems.add(new StudentByItem(row));
        }

        return studenBytItems;

    }

    public void setRowsByPartKey(Session targetSession, List<DataObject> inputList) {

        List<DataObject> outputList = new ArrayList<>();

        for (DataObject stbi : inputList) {

            StudentByItem newStbi = convertRow(stbi);
            outputList.add(newStbi);

        }

        List<Statement> inserts = statementCreator.createStudentByItemInsert(outputList);
        BatchStatement batch = new BatchStatement();
        batch.setConsistencyLevel(ConsistencyLevel.QUORUM);

        for (Statement insert : inserts) {

            batch.add(insert);
        }

        targetSession.execute(batch);
    }

    // href from existing object gets assignesd to cnthref
    // ctype is assigned the static value from CONTEXT_TYPE_DEFAULT within DataObject

    public StudentByItem convertRow(DataObject stbi) {

        String cnttype = null;
        String cntid = null;
        Date duedate = null;

        StudentByItem tempStbi = ((StudentByItem) stbi);

        return new StudentByItem(
                tempStbi.getCid()
                ,tempStbi.getCtype()
                ,tempStbi.getId()
                ,tempStbi.getDsc()
                ,cnttype
                ,cntid
                ,tempStbi.getHref()
                ,tempStbi.getLmd()
                ,tempStbi.getLvl()
                ,tempStbi.getRpid()
                ,tempStbi.getSo()
                ,tempStbi.getTn()
                ,tempStbi.getTtl()
                ,duedate
        );

    }
}
