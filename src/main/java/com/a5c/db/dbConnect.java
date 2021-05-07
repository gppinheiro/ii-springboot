package com.a5c.db;

import com.a5c.data.Transform;
import com.a5c.data.Unload;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

/**
 * Class dbConnect is used to: communicate with Heroku DataBase and create a Prepared Statements to send to the DB.
 * Date: March-3-2021.
 * @author Group A5_C.
 */
public class dbConnect {
    /**
     * Store Heroku URI: User + Password + Host + Port + Database.
     * Private - Nobody needs to know what URI we are using.
     * Static and Final - Never changes.
     */
    private static final String heroku_url="postgres://vkgvpttoqwwjti:241f4c5e49e1ff17e84ecab4bbe8c63ead0a80d1684405a3c5fa8542f36de5c0@ec2-54-74-35-87.eu-west-1.compute.amazonaws.com:5432/d2j57fljq86oa0";
    /**
     * Store DB Connection.
     * Private - Nobody needs to know the db connection.
     */
    private Connection conn = null;
    private final Timestamp initTime;

    /**
     * Constructor that creates connection with DataBase.
     */
    public dbConnect() {
        this.initTime = new Timestamp(System.currentTimeMillis());
        try {
            this.conn = getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to make first connection.
     * @return DB Connection.
     * @throws URISyntaxException if occurs an URI error in syntax.
     * @throws SQLException if occurs an error in DB.
     */
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(heroku_url);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public int TransformLength() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT COUNT(*) FROM ii.\"Transform\";");
        ResultSet rs = s.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public String getTransform() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT \"OrderNumber\", \"from\", \"to\", quantity, time, \"MaxDelay\", penalty, \"timeMES\" FROM ii.\"Transform\";");
        ResultSet rs = s.executeQuery();

        Transform[] Transforms = new Transform[TransformLength()];

        String res = "";

        int i=0;
        while (rs.next()) {
            Transforms[i] = new Transform(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7));
            Transforms[i].setTimeMES(rs.getInt(8));
            Transforms[i].setInitPenalty(rs.getInt(7));

            res += (Transforms[i].toString());
            res += '\n';

            i++;
        }

        if ( res.equals("") ) {
            res = "Nothing to do.";
        }

        return res;
    }

    public int ElapseTransformLength() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT COUNT(*) FROM ii.\"ElapseTransform\";");
        ResultSet rs = s.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public String getElapseTransform() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT \"OrderNumber\", \"from\", \"to\", quantity, time, \"MaxDelay\", penalty, \"timeMES\" FROM ii.\"ElapseTransform\";");
        ResultSet rs = s.executeQuery();

        Transform[] Transforms = new Transform[ElapseTransformLength()];

        String res = "";

        int i=0;
        while (rs.next()) {
            Transforms[i] = new Transform(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7));
            Transforms[i].setTimeMES(rs.getInt(8));
            Transforms[i].setInitPenalty(rs.getInt(7));

            res += (Transforms[i].toString());
            res += '\n';

            i++;
        }

        if ( res.equals("") ) {
            res = "Nothing is doing.";
        }

        return res;
    }

    public int EndTransformLength() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT COUNT(*) FROM ii.\"EndTransform\";");
        ResultSet rs = s.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public String getEndTransform() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT \"OrderNumber\", \"from\", \"to\", quantity, time, \"MaxDelay\", penalty, \"timeMES\", \"InitialPenalty\", \"st\", \"et\", \"TransformTime\" FROM ii.\"EndTransform\";");
        ResultSet rs = s.executeQuery();

        Transform[] Transforms = new Transform[EndTransformLength()];

        String res = "";

        int i=0;
        while (rs.next()) {
            Transforms[i] = new Transform(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7));
            Transforms[i].setTimeMES(rs.getInt(8));
            Transforms[i].setInitPenalty(rs.getInt(9));
            Transforms[i].setST(rs.getInt(10));
            Transforms[i].setET(rs.getInt(11));
            Transforms[i].setTT(rs.getInt(12));

            res += (Transforms[i].toString());
            res += '\n';

            i++;
        }

        if ( res.equals("") ) {
            res = "Nothing is done.";
        }

        return res;
    }

    public String getUnload() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT COUNT(*) FROM ii.\"Unload\";");
        ResultSet rs = s.executeQuery();
        rs.next();
        Unload[] Unloads = new Unload[rs.getInt(1)];

        s = this.conn.prepareStatement("SELECT \"OrderNumber\", type, destination, quantity FROM ii.\"Unload\";");
        rs = s.executeQuery();

        String res = "";

        int i=0;
        while (rs.next()) {
            Unloads[i] = new Unload(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4));

            res += (Unloads[i].toString());
            res += '\n';

            i++;
        }

        if ( res.equals("") ) {
            res = "Nothing to unload.";
        }

        return res;
    }

    public String getUnloadDone() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT COUNT(*) FROM ii.\"EndUnload\";");
        ResultSet rs = s.executeQuery();
        rs.next();
        Unload[] Unloads = new Unload[rs.getInt(1)];

        s = this.conn.prepareStatement("SELECT \"OrderNumber\", type, destination, quantity FROM ii.\"EndUnload\";");
        rs = s.executeQuery();

        String res = "";

        int i=0;
        while (rs.next()) {
            Unloads[i] = new Unload(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4));

            res += (Unloads[i].toString());
            res += '\n';

            i++;
        }

        if ( res.equals("") ) {
            res = "Nothing to unload.";
        }

        return res;
    }

    public String getCurrentStores() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT type, quantity FROM ii.\"CurrentStores\";");
        ResultSet rs = s.executeQuery();

        String res = "";

        while (rs.next()) {
            res += "Type: "+rs.getInt(1)+"& Quantity: "+rs.getInt(2)+"\n";
        }

        return res;
    }

    public String getMachines() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT \"machine\", t1, t2, t3, t4, t5, t6, t7, t8, \"total\" FROM ii.\"MachinesStatistic\";");
        ResultSet rs = s.executeQuery();

        String res = "";

        while (rs.next()) {
            res += "Machine: "+rs.getString(1)+"-> T1: "+rs.getInt(2)+"& T2: "+rs.getInt(3)+"& T3: "+rs.getInt(4)+"& T4: "+rs.getInt(5)+"& T5: "+rs.getInt(6)+"& T6: "+rs.getInt(7)+"& T7: "+rs.getInt(8)+"& T8: "+rs.getInt(9)+"& TOTAL: "+rs.getInt(10)+"\n";
        }

        return res;
    }

    public String getPushers() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT \"pusher\", p1, p2, p3, p4, p5, p6, p7, p8, p9, \"total\" FROM ii.\"PushersStatistic\";");
        ResultSet rs = s.executeQuery();

        String res = "";

        while (rs.next()) {
            res += "Pusher: "+rs.getString(1)+"-> P1: "+rs.getInt(2)+"& P2: "+rs.getInt(3)+"& P3: "+rs.getInt(4)+"& P4: "+rs.getInt(5)+"& P5: "+rs.getInt(6)+"& P6: "+rs.getInt(7)+"& P7: "+rs.getInt(8)+"& P8: "+rs.getInt(9)+"& P9: "+rs.getInt(10)+"& TOTAL: "+rs.getInt(11)+"\n";
        }

        return res;
    }

    public String getExcepted() throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("SELECT \"from\", \"to\", \"tt\" FROM ii.\"ExceptedPenalty\";");
        ResultSet rs = s.executeQuery();

        String res = "";

        while (rs.next()) {
            res += "FROM: "+rs.getInt(1)+" TO: "+rs.getInt(2)+" TT: "+rs.getInt(3)+" //\n";
        }

        return res;
    }

    public void addTransform(Transform tf) throws SQLException {
        PreparedStatement s = this.conn.prepareStatement("INSERT INTO ii.\"Transform\" VALUES (?,?,?,?,?,?,?,?,?,?);");
        s.setInt(1,tf.getOrderNumber());
        s.setInt(2,tf.getFrom());
        s.setInt(3,tf.getTo());
        s.setInt(4,tf.getQuantity());
        s.setInt(5,tf.getTime());
        s.setInt(6,tf.getMaxDelay());
        s.setInt(7,tf.getPenalty());
        s.setTimestamp(8,new Timestamp(System.currentTimeMillis()));
        s.setInt(9,0);
        s.setInt(10,0);
        s.executeUpdate();
    }

}
