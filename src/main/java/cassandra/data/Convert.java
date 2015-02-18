package cassandra.data;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colemw on 9/25/14.
 *
 *
 */

public interface Convert {

    public List getRowsByPartKey(String cid, Session sourceSession);

    public void setRowsByPartKey(Session targetSession, List<DataObject> inputList);

    public Object convertRow(DataObject data);

}
