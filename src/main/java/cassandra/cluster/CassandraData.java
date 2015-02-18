package cassandra.cluster;

/**
 * Created by colemw on 5/20/14.
 *
 * Creates a new Cassandra Session with host and/or port information specified.
 *
 */

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.ArrayList;

public class CassandraData {

    private Session cassandraSession = null;
    private  Cluster cluster;

    public CassandraData(String host) {

        createContactPoint(host);
    }

    public CassandraData(ArrayList<String> hosts) {

        createContactPoints(hosts);
    }

    public Session getSession() {

        if (cassandraSession == null) {
            cassandraSession = createSession();
        }

        return cassandraSession;

    }

    protected Session createSession() {

        return cluster.connect();
    }

    public void createContactPoint(String host) {

        cluster = Cluster.builder().addContactPoint(host).withPort(9042).build();

    }

    public void createContactPoints(ArrayList<String> hostList) {

        Cluster.Builder builder = new Cluster.Builder();

        for (String nodeAddress : hostList) {

            builder = Cluster.builder().addContactPoint(nodeAddress).withPort(9042);

        }

        cluster = builder.build();

    }

    public Session createSession(String keyspace) {

        return cluster.connect(keyspace);
    }

    public String getCassandraInfo() {

        Row row = getSession().execute("select cluster_name, release_version from system.local").one();

        return "Release: " + row.getString("release_version") + " | Cluster: " + row.getString("cluster_name");
    }
}

