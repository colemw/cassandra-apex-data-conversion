package cassandra.data;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colemw on 10/7/14.
 *
 * Class to define and send prepared statements to Cassandra when the application loads
 *
 */

public class CourseStructureStatementCreator implements StatementCreator {

    static final String STUDENT_ITEMS_SELECT = "SELECT * FROM studentitems WHERE cid = ?";
    static final String STUDENT_ITEMS_INSERT =  "INSERT INTO coursestructure.studentitems (cid, ctype, so, id, dsc, cnttype, cntid, cnthref, lmd, lvl, rpid, tn, ttl, duedate) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    static final String STUDENT_BY_ITEM_SELECT = "SELECT * FROM studentbyitem WHERE cid = ?";
    static final String STUDENT_BY_ITEM_INSERT =  "INSERT INTO coursestructure.studentbyitem (cid, ctype, id, dsc, cnttype, cntid, cnthref, lmd, lvl, rpid, so, tn, ttl, duedate) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    static final String NON_STUDENT_ITEMS_SELECT = "SELECT * FROM nonstudentitems WHERE cid = ?";
    static final String NON_STUDENT_ITEMS_INSERT =  "INSERT INTO coursestructure.nonstudentitems (cid, ctype, so, id, dsc, cnttype, cntid, cnthref, lmd, lvl, rpid, tn, ttl, duedate, lock) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    static final String NON_STUDENT_BY_ITEM_SELECT = "SELECT * FROM nonstudentbyitem WHERE cid = ?";
    static final String NON_STUDENT_BY_ITEM_INSERT =  "INSERT INTO coursestructure.nonstudentbyitem (cid, ctype, id, dsc, cnttype, cntid, cnthref, lmd, lvl, rpid, so, tn, ttl, duedate, lock) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private PreparedStatement studentItemsSelect;
    private PreparedStatement studentItemsInsert;
    private PreparedStatement studentByItemSelect;
    private PreparedStatement studentByItemInsert;
    private PreparedStatement nonStudentItemsSelect;
    private PreparedStatement nonStudentItemsInsert;
    private PreparedStatement nonStudentByItemSelect;
    private PreparedStatement nonStudentByItemInsert;

    public CourseStructureStatementCreator (Session sourceSession, Session targetSession) {

        prepareStatements(sourceSession, targetSession);

    }

    private void prepareStatements(Session sourceSession, Session targetSession) {

        studentItemsSelect = sourceSession.prepare(STUDENT_ITEMS_SELECT);
        studentItemsInsert = targetSession.prepare(STUDENT_ITEMS_INSERT);
        studentByItemSelect = sourceSession.prepare(STUDENT_BY_ITEM_SELECT);
        studentByItemInsert = targetSession.prepare(STUDENT_BY_ITEM_INSERT);
        nonStudentItemsSelect = sourceSession.prepare(NON_STUDENT_ITEMS_SELECT);
        nonStudentItemsInsert = targetSession.prepare(NON_STUDENT_ITEMS_INSERT);
        nonStudentByItemSelect = sourceSession.prepare(NON_STUDENT_BY_ITEM_SELECT);
        nonStudentByItemInsert = targetSession.prepare(NON_STUDENT_BY_ITEM_INSERT);

    }

    public Statement createStudentItemsSelect(String key) {

        return studentItemsSelect.bind(key);
    }

    public Statement createStudentByItemSelect(String key) {

        return studentByItemSelect.bind(key);
    }

    public Statement createNonStudentItemsSelect(String key) {

        return nonStudentItemsSelect.bind(key);
    }

    public Statement createNonStudentByItemSelect(String key) {

        return nonStudentByItemSelect.bind(key);
    }

    public List<Statement> createStudentItemsInsert(List<DataObject> inputList) {

        List<Statement> result = new ArrayList<>();

        for (DataObject studentItem : inputList) {

            StudentItemsDAO sti = (StudentItemsDAO) studentItem;

            result.add(studentItemsInsert.bind(
                    sti.getCid()
                    ,sti.getCtype()
                    ,sti.getSo()
                    ,sti.getId()
                    ,sti.getDsc()
                    ,sti.getCnttype()
                    ,sti.getCntid()
                    ,sti.getCnthref()
                    ,sti.getLmd()
                    ,sti.getLvl()
                    ,sti.getRpid()
                    ,sti.getTn()
                    ,sti.getTtl()
                    ,sti.getDuedate())
            );

        }

        return result;
    }

    public List<Statement> createStudentByItemInsert(List<DataObject> inputList) {

        List<Statement> result = new ArrayList<>();

        for (DataObject studentByItem : inputList) {

            StudentByItemDAO stbi = (StudentByItemDAO) studentByItem;

            result.add(studentByItemInsert.bind(
                    stbi.getCid()
                    ,stbi.getCtype()
                    ,stbi.getId()
                    ,stbi.getDsc()
                    ,stbi.getCnttype()
                    ,stbi.getCntid()
                    ,stbi.getCnthref()
                    ,stbi.getLmd()
                    ,stbi.getLvl()
                    ,stbi.getRpid()
                    ,stbi.getSo()
                    ,stbi.getTn()
                    ,stbi.getTtl()
                    ,stbi.getDuedate())
            );

        }

        return result;
    }

    public List<Statement> createNonStudentItemsInsert(List<DataObject> inputList) {

        List<Statement> result = new ArrayList<>();

        for (DataObject nonStudentItem : inputList) {

            NonStudentItemsDAO nsti = (NonStudentItemsDAO) nonStudentItem;

            result.add(nonStudentItemsInsert.bind(
                    nsti.getCid()
                    ,nsti.getCtype()
                    ,nsti.getSo()
                    ,nsti.getId()
                    ,nsti.getDsc()
                    ,nsti.getCnttype()
                    ,nsti.getCntid()
                    ,nsti.getCnthref()
                    ,nsti.getLmd()
                    ,nsti.getLvl()
                    ,nsti.getRpid()
                    ,nsti.getTn()
                    ,nsti.getTtl()
                    ,nsti.getDuedate()
                    ,nsti.getLock())
            );

        }

        return result;
    }

    public List<Statement> createNonStudentByItemInsert(List<DataObject> inputList) {

        List<Statement> result = new ArrayList<>();

        for (DataObject nonStudentByItem : inputList) {

            NonStudentByItemDAO nstbi = (NonStudentByItemDAO) nonStudentByItem;

            result.add(nonStudentByItemInsert.bind(
                    nstbi.getCid()
                    ,nstbi.getCtype()
                    ,nstbi.getId()
                    ,nstbi.getDsc()
                    ,nstbi.getCnttype()
                    ,nstbi.getCntid()
                    ,nstbi.getCnthref()
                    ,nstbi.getLmd()
                    ,nstbi.getLvl()
                    ,nstbi.getRpid()
                    ,nstbi.getSo()
                    ,nstbi.getTn()
                    ,nstbi.getTtl()
                    ,nstbi.getDuedate()
                    ,nstbi.getLock())
            );

        }

        return result;
    }

}
