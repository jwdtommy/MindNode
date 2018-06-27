package dd.com.mindnode.nodeview;

public class Node {
    public Node mLastNode;
    public Node mNextNode;
    public Line mLine;
    public Border mBorder;
    public Content mContent;

    public static class Border {
        public int mColor;
        public int mWidth;
    }

    public static class Content {
        public String mText;
        public String mColor;
    }

    public class Line {
        public int mColor;
    }

}
