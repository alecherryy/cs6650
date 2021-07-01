package server.model;
import java.sql.*;

/**
 * This class represents a database. In this case, it's a MySQL database.
 */
public class Database {
    Connection conn;

    /**
     * Class constructor.
     */
    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bdsd_db", "root", "password");
            conn.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create statement to add a new record to the database.
     *
     * @param key of the record to be created
     * @param val of the record's column
     * @return result of the query
     */
    public int createRecord(String key, int val) {
        int result;
        String query = "INSERT INTO words (wid, total) VALUES ('" + key + "', " + val + ");";

        try {
            PreparedStatement st = this.conn.prepareStatement(query);
            st.executeUpdate();
            this.conn.commit();
//            this.conn.close();
            result = 1;
        } catch (SQLException e) {
            result = -1;
        }

        return result;
    }

    /**
     * Read statement to retrieve a record from the database.
     *
     * @param key of the record to be retrieved
     * @return result of the query
     */
    public int readRecord(String key) {
        String query = "SELECT * FROM words WHERE wid = '" + key + "';";

        try {
            ResultSet rs =  this.conn.createStatement().executeQuery(query);

            if (rs.next()) {
                // get total count for word
                int val = rs.getInt(2);
                return val;
            }
        } catch (SQLException e) {
            // do nothing and go to return statement
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Update statement to update an exhisting record in the database.
     *
     * @param key of the record to be updated
     * @param val to update the record with
     * @return result of the query
     */
    public int updateRecord(String key, int val) throws SQLException {
        int result;
        int num = readRecord(key);
        int total = num + val;
        String query = "UPDATE words SET total = " + total + "/+" + val  + " WHERE wid = '" + key + "';";

        try {
            PreparedStatement st = this.conn.prepareStatement(query);
            st.executeUpdate();
            this.conn.commit();
//            this.conn.close();
            result = 1;
        } catch (SQLException e) {
            result = -1;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Delete a record from the database.
     *
     * @param key of the record to be deleted
     */
    public void deleteRecord(String key) {
        // no use for this, right now
    }
 }