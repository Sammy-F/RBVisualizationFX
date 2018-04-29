package visualization;

import java.util.ArrayList;

public class ModificationLog {

    private ArrayList<Modification> modList;
    private static StringBuilder strLog;

    public ModificationLog() {
        modList = new ArrayList<>();
    }

    //Userful public static methods

    public ArrayList<Modification> getLogArray() {
        if (modList == null) {
            modList = new ArrayList<>();
        }

        return modList;
    }

    public void addChange(Modification mod) {
        if (modList == null) {
            modList = new ArrayList<>();
        }

        modList.add(mod);
    }

    public void addChange(int caseVal, double nodeVal) {
        if (modList == null) {
            System.out.println("Initializing mod log");
            modList = new ArrayList<>();
            modList.add(new Modification(caseVal, nodeVal));
        } else {
            System.out.println("Adding mod to log");
            modList.add(new Modification(caseVal, nodeVal));
        }
    }

    public Modification removeChange() {
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

            for (Modification tModification : modList) {
                System.out.println(tModification.toString());
                strLog.append(changeNum + ": " + tModification.toString() + "\n");
                changeNum++;
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
