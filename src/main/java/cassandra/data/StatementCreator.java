package cassandra.data;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

import java.util.List;

/**
 * Created by colemw on 10/7/14.
 *
 */

public interface StatementCreator {

    Statement createStudentItemsSelect(String key);

    List<Statement> createStudentItemsInsert(List<DataObject> inputList);

    Statement createNonStudentItemsSelect(String key);

    List<Statement> createNonStudentItemsInsert(List<DataObject> inputList);

    Statement createStudentByItemSelect(String key);

    List<Statement> createStudentByItemInsert(List<DataObject> inputList);

    Statement createNonStudentByItemSelect(String key);

    List<Statement> createNonStudentByItemInsert(List<DataObject> inputList);

}
