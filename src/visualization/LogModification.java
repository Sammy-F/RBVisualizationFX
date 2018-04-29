package visualization;

public class LogModification {

    //CASES

    public final static int NODEISNIL = 0;   //the node we want to insert does not exist OR is outside the range [0,999]
    public final static int NODEEXISTS = 12;
    public final static int INSERTION = 9;
    public final static int DELETION = 10;
    public final static int INSERTC1 = 1;   //inserted node's aunt is red
    public final static int INSERTC2 = 2;   //inserted node is a right child and its aunt is black
    public final static int INSERTC3 = 3;   //inserted node is a left child and its aunt is black
    public final static int DELETEC1 = 4;   //deleted node's sibling is red
    public final static int DELETEC2 = 5;   //deleted node's sibling is black; sibling's children both black
    public final static int DELETEC3 = 6;   //deleted node's sibling is black; sibling's left child red, right black
    public final static int DELETEC4 = 7;   //deleted node's sibling is black; sibling's right child red, left black
    public final static int INSERTROOT = 8; //we generate the root
    public final static int NOCASE = -1;    //we are not even trying to insert (for insCase) or to delete (for delCase)
    public final static int INVALIDINPUT = 11;

    public final static int TRANSPLANT = 13;
    public final static int MAKEROOT = 14;

    public final static int RIGHTROTATE = 15;
    public final static int LEFTROTATE = 16;

    //Instance variables

    private int caseVal;
    private double nodeVal;

    private double vNodeVal;
    private double uNodeVal;

    public LogModification(int caseVal, double uNodeVal, double vNodeVal) {
        this.caseVal = caseVal;
        this.uNodeVal = uNodeVal;
        this.vNodeVal = vNodeVal;
        nodeVal = -1;
    }

    public LogModification(int caseVal, double nodeVal) {
        this.caseVal = caseVal;
        this.nodeVal = nodeVal;

        uNodeVal = -1;
        vNodeVal = -1;
    }

    public String toString() {

        StringBuilder mStr = new StringBuilder();

        switch(caseVal) {
            case NODEISNIL:
                mStr.append("Node does not exist or is outside the range.");
                break;
            case INSERTC1:
                mStr.append("The inserted node's great aunt is red"); //Shouldn't it be great aunt since it's the parent's parent's sister?
                break;
            case INSERTC2:
                mStr.append("The inserted node is a right child and its great aunt is black.");
                break;
            case INSERTC3:
                mStr.append("The inserted node is a left child and its great aunt is black.");
                break;
            case DELETEC1:
                mStr.append("The deleted node's sibling is red.");
                break;
            case DELETEC2:
                mStr.append("The deleted node's sibling is black and both of the sibling's children are black.");
                break;
            case DELETEC3:
                mStr.append("The deleted node's sibling is black and the sibling has a black left child and red right child.");
                break;
            case DELETEC4:
                mStr.append("The deleted node's sibling is black and the sibling has a red right child and black left child.");
                break;
            case INSERTROOT:
                mStr.append("Inserting a root.");
                break;
            case NOCASE:
                mStr.append("An error occurred.");
                break;
            case INSERTION:
                mStr.append("Attempting to insert: " + nodeVal);
                break;
            case DELETION:
                mStr.append("Attempting to delete: " + nodeVal);
                break;
            case INVALIDINPUT:
                mStr.append("Invalid input. Please insert an numeric value between 0 and 999.");
                break;
            case NODEEXISTS:
                mStr.append("Node already exists.");
                break;
            case TRANSPLANT:
                mStr.append("Transplanting " + uNodeVal + " and " + vNodeVal + ".");
                break;
            case MAKEROOT:
                mStr.append("Make" + nodeVal + " the root");
                break;
            case LEFTROTATE:
                mStr.append("Perform left rotation");
                break;
            case RIGHTROTATE:
                mStr.append("Perform right rotation");

        }

        return mStr.toString();
    }

    public int getCaseVal() { return caseVal; }

}
