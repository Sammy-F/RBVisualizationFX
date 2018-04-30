package visualization;

/**
 * Class for tree modifications
 *
 * Implemented so that we can keep track of the modifications made to a tree to allow the easy creation of a copy of a
 * tree that is its own distinct object (by repopulating that tree with the same modifications used on the orginal tree)
 *
 * Authors: Samantha Fritsche and Katya Gurgel
 */
public class TreeModification<T> {

    private T key;

    private boolean insert;

    private boolean delete;

    /**
     * Public constructor for treeModification
     *
     * @param key       the key inserted/deleted in this step
     * @param insert    boolean whether this modification was an insert
     */
    public TreeModification(T key, boolean insert) {
        this.key = key;
        this.insert = insert;
        this.delete = !insert;
    }

    //Public getters:

    public T getKey() {
        return key;
    }

    public boolean isInsert() {
        return insert;
    }

    public boolean isDelete() {
        return delete;
    }

}
