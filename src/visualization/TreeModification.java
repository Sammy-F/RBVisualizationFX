package visualization;

/**
 * Created by katya on 4/24/2018.
 */
public class TreeModification {

    private double val;

    private boolean insert;

    private boolean delete;

    public TreeModification(double val, boolean insert) {
        this.val = val;
        this.insert = insert;
        this.delete = !insert;
    }

    public double getVal() {
        return val;
    }


    public boolean isInsert() {
        return insert;
    }

    public boolean isDelete() {
        return delete;
    }

}
