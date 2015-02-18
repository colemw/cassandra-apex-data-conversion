package cassandra.data;

import com.datastax.driver.core.Row;

import java.util.Date;
import java.util.UUID;

/**
 * Created by colemw on 9/26/14.
 *
 * Data access object class (pre and post migration) for NonStudentByItem
 *
 */

public class NonStudentByItemDAO implements DataObject {

    // Exisiting data fields
    private String cid;
    private UUID id;
    private String dsc;
    private String href;
    private Date lmd;
    private boolean lock;
    private int lvl;
    private UUID rpid;
    private int so;
    private String tn;
    private String ttl;

    // New data fields
    private String ctype;
    private String cnttype;
    private String cntid;
    private String cnthref;
    private Date duedate;

    // Incoming data constructor
    public NonStudentByItemDAO(Row row) {

        cid = row.getString("cid");
        so = row.getInt("so");
        id = row.getUUID("id");
        dsc = row.getString("dsc");
        href = row.getString("href");
        lmd = row.getDate("lmd");
        lock = row.getBool("lock");
        lvl = row.getInt("lvl");
        rpid = row.getUUID("rpid");
        tn = row.getString("tn");
        ttl = row.getString("ttl");

    }

    // Outbound data constructor
    public NonStudentByItemDAO(String cid, String ctype, UUID id, String dsc, String cnttype, String cntid, String cnthref,
                                Date lmd, int lvl, UUID rpid, int so, String tn, String ttl, Date duedate, boolean lock) {

        this.cid = cid;
        this.ctype = ctype;
        this.so = so;
        this.id = id;
        this.dsc = dsc;
        this.cnttype = cnttype;
        this.cntid = cntid;
        this.cnthref = cnthref;
        this.lmd = lmd;
        this.lvl = lvl;
        this.rpid = rpid;
        this.tn = tn;
        this.ttl = ttl;
        this.duedate = duedate;
        this.lock = lock;

    }

    public String getCid() { return this.cid; }

    public int getSo() { return this.so; }

    public UUID getId() { return this.id; }

    public String getDsc() { return this.dsc; }

    public String getHref() { return this.href; }

    public Date getLmd() { return this.lmd; }

    public boolean getLock() { return this.lock; }

    public int getLvl() { return this.lvl; }

    public UUID getRpid() { return this.rpid; }

    public String getTn() { return this.tn; }

    public String getTtl() { return this.ttl; }

    public String getCtype() { return CONTEXT_TYPE_DEFAULT; }

    public String getCnttype() { return this.cnttype; }

    public String getCntid() { return this.cntid; }

    public String getCnthref() { return this.cnthref; }

    public Date getDuedate() { return this.duedate; }
}
