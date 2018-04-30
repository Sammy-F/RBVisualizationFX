package visualization;

import java.util.ArrayList;

/**
 * Authors: Samantha Fritsche and Katya Gurgel
 *
 * This class stores our list of CaseMods and handles
 * formatting them into a String to display in our ScrollPane
 */
public class CaseModLog {

    //INSTANCE VARIABLES

    private ArrayList<CaseMod> modList;
    private static StringBuilder strLog;

    public CaseModLog() {
        modList = new ArrayList<>();
    }

    //PUBLIC METHODS

    /**
     * Return the ArrayList of CaseMods
     * @return
     */
    public ArrayList<CaseMod> getLogArray() {
        if (modList == null) {
            modList = new ArrayList<>();
        }

        return modList;
    }

    /**
     * Add a CaseMod that requires two values (i.e transplant)
     * @param caseVal
     * @param uVal
     * @param vVal
     */
    public void addChange(int caseVal, double uVal, double vVal) {

        if (modList == null) {
            modList = new ArrayList<>();
            modList.add(new CaseMod(caseVal, uVal, vVal));
        } else {
            modList.add(new CaseMod(caseVal, uVal, vVal));
        }

    }

    /**
     * Add an existing CaseMod to the modList Array
     * @param mod
     */
    public void addChange(CaseMod mod) {
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
//            System.out.println("Initializing mod log");
            modList = new ArrayList<>();
            modList.add(new CaseMod(caseVal, nodeVal));
        } else {
//            System.out.println("Adding mod to log");
            modList.add(new CaseMod(caseVal, nodeVal));
        }
    }

    /**
     * Converts the CaseModLog into a String representation that can be read into a
     * graphical representation (i.e. a Label).
     * @return
     */
    public String getLogString() {

//        System.out.println(modList.toString());

        if (modList != null && modList.size() > 0) {
            if (strLog == null) {
                strLog = new StringBuilder();
            } else {
                strLog.setLength(0);
            }
            int changeNum = 0;

            for (CaseMod tCaseMod : modList) {
                if (tCaseMod.getCaseVal() == CaseMod.INSERTION ||
                        tCaseMod.getCaseVal() == CaseMod.DELETION ||
                        tCaseMod.getCaseVal() == CaseMod.NOCASE ||
                        tCaseMod.getCaseVal() == CaseMod.INVALIDINPUT) {

//                    System.out.println(tCaseMod.toString());
                    strLog.append(changeNum + ": " + tCaseMod.toString() + "\n");
                    changeNum++;

                } else {
//                    System.out.println(tCaseMod.toString());
                    strLog.append("   " + tCaseMod.toString() + "\n");
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
