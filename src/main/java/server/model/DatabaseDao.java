package server.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDao {
    private final static Database db = new Database();
    public DatabaseDao() {}

    public void addRecord(String str) throws SQLException {
        // split string to isolate values
        String[] pairs = str.split(",");

        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split("=");

            // add record to database
            int result = db.createRecord(keyValue[0], Integer.parseInt(keyValue[1]));

            if (result < 0) {
                db.updateRecord(keyValue[0], Integer.parseInt(keyValue[1]));
            }
        }
    }
}
