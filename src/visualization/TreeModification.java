package visualization;

/**
 * Created by katya on 4/24/2018.
 */
public class TreeModification<T> {

    private T key;

    private boolean insert;

    private boolean delete;

    public TreeModification(T key, boolean insert) {
        this.key = key;
        this.insert = insert;
        this.delete = !insert;
    }

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
