package visualization;

import java.util.ArrayList;

/**
 * This class stores our list of LogModifications and handles
 * formatting them into a String to display in our ScrollPane
 */
public class ModificationLog {

    //INSTANCE VARIABLES

    private ArrayList<LogModification> modList;
    private static StringBuilder strLog;

    public ModificationLog() {
        modList = new ArrayList<>();
    }

    //PUBLIC METHODS

    /**
     * I mostly added this so that we can implement some kind of animation  (i.e. highlighting text).
     * For now, the new LogModifications basically just pops up, like the tree.
     * @return
     */
    public ArrayList<LogModification> getLogArray() {
        if (modList == null) {
            modList = new ArrayList<>();
        }

        return modList;
    }

    /**
     * Used for modifications that take 2 node values
     * @param caseVal
     * @param uVal
     * @param vVal
     */
    public void addChange(int caseVal, double uVal, double vVal) {

        if (modList == null) {
            System.out.println("Initializing mod log");
            modList = new ArrayList<>();
            modList.add(new LogModification(caseVal, uVal, vVal));
        } else {
            System.out.println("Adding mod to log");
            modList.add(new LogModification(caseVal, uVal, vVal));
        }

    }

    /**
     * Add a LogModification to the modList Array
     * @param mod
     */
    public void addChange(LogModification mod) {
        if (modList == null) {
            modList = new ArrayList<>();
        }

        modList.add(mod);
    }

    /**
     * Add a change from a case value and node value
     * @param caseVal
     * @param nodeVal
     */
    public void addChange(int caseVal, double nodeVal) {
        if (modList == null) {
            System.out.println("Initializing mod log");
            modList = new ArrayList<>();
            modList.add(new LogModification(caseVal, nodeVal));
        } else {
            System.out.println("Adding mod to log");
            modList.add(new LogModification(caseVal, nodeVal));
        }
    }

    /**
     * Remove and return the lest LogModification in modList
     * @return LogModification oldMod
     */
    public LogModification removeChange() {
        if (modList == null) {
            modList = new ArrayList<>();
            return null;
        } else if (modList.size() > 0) {
            return modList.remove(modList.size()-1);
        } else {
            return null;
        }
    }

    /**
     * Converts the ModificationLog into a String that can be read into a
     * graphical representation.
     * @return
     */
    public String getLogString() {

        System.out.println(modList.toString());

        if (modList != null && modList.size() > 0) {
            if (strLog == null) {
                strLog = new StringBuilder();
            } else {
                strLog.setLength(0);
            }
            int changeNum = 0;

            for (LogModification tLogModification : modList) {
                if (tLogModification.getCaseVal() == LogModification.INSERTION ||
                        tLogModification.getCaseVal() == LogModification.DELETION ||
                        tLogModification.getCaseVal() == LogModification.NODEISNIL ||
                        tLogModification.getCaseVal() == LogModification.NODEEXISTS ||
                        tLogModification.getCaseVal() == LogModification.NOCASE ||
                        tLogModification.getCaseVal() == LogModification.INVALIDINPUT) {

                    System.out.println(tLogModification.toString());
                    strLog.append(changeNum + ": " + tLogModification.toString() + "\n");
                    changeNum++;

                } else {
                    System.out.println(tLogModification.toString());
                    strLog.append("   " + tLogModification.toString() + "\n");
                }
            }

            return strLog.toString();
        } else {
            return "Try adding or deleting a node to see what happens!";
        }
    }

    public void clearLog() {
        modList.clear();
    }
}
