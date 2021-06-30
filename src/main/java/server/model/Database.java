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
        String query = "INSERT INTO words (wid, total) VALUES ('" + key + "', " + val + ");";

        try {
            int result = this.conn.createStatement().executeUpdate(query);
            return result;
        } catch (SQLException e) {
            // do nothing and go to return statement
        }

        return -1;
    }

    /**
     * Read statement to retrieve a record from the database.
     *
     * @param key of the record to be retrieved
     * @return result of the query
     */
    public ResultSet readRecord(String key) {
        String query = "SELECT * FROM words WHERE wid = '" + key + "';";

        try {
            return this.conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            // do nothing and go to return statement
        }

        return null;
    }

    /**
     * Update statement to update an exhisting record in the database.
     *
     * @param key of the record to be updated
     * @param val to update the record with
     * @return result of the query
     */
    public int updateRecord(String key, int val) throws SQLException {
        int result = readRecord(key).findColumn("total");
        int total = result + val;
        String query = "UPDATE words SET total = " + total + "/+" + val  + " WHERE wid = '" + key + "';";

        try {
            return this.conn.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            // do nothing and go to return statement
        }

        return -1;
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