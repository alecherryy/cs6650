package server.model;

import java.util.HashMap;
import java.util.Locale;

/**
 * This class represents a Task. The object can perform various
 * operations such as counting words, counting character,
 * check for duplicate terms, etc.
 */
public class Task {

    /**
     * Class constructor.
     */
    public Task() {}

    /**
     * Given a string, split it and return each
     * word (and special character) in a tuple.
     *
     * @param str to split
     * @retrun a tuple
     */
    public HashMap<String, Integer> countWords(String str) {
        HashMap<String, Integer> tuple = new HashMap();
        for (String s : str.split("\\s+")) {
            if (!s.isEmpty()) {
                // ignore casing
                tuple.put(s.toLowerCase(Locale.ROOT), tuple.getOrDefault(s, 0) + 1);
            }
        }

        return tuple;
    }

    /**
     * Given a string, count the total number of characters.
     *
     * @param str to split
     */
    public void countChars(String str) {
        // this method does not do anything for now
    }
}
