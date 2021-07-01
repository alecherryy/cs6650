package server.model;

import java.sql.SQLException;

public class DatabaseDao {
    private Database db;

    public DatabaseDao() {
        db = new Database();
    }

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

    /**
     * Return total count of given word.
     *
     * @param word to retrieve from the database
     * @return the total count of the word
     */
    public int readRecord(String word) {
        return db.readRecord(word);
    }
}
