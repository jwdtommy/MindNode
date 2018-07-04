package dd.com.mindnode.nodeview;

import android.content.Context;
import android.graphics.RectF;

public class MindNodeEnv {

    private float mScaleFactor = 1;
    private float mScale = 1;
    private float mLastScale = 1;
    private float mScalePivotX;
    private float mScalePivotY;
    private float mDistanceX;
    private float mDistanceY;
    private float mLastDistanceX;
    private float mLastDistanceY;

    private static MindNodeEnv mindNodeEnv;

    private MindNodeEnv() {

    }

    public static MindNodeEnv getEnv() {
        if (mindNodeEnv == null) {
            mindNodeEnv = new MindNodeEnv();
        }
        return mindNodeEnv;
    }

    /**
     * 双指放大缩小，单指空白区域平移后会导致canvas发生scale和translate，导致NodeView的肉眼坐标实际已经变化了。
     * @param srcRect
     * @return
     */
    public RectF getTransRect(RectF srcRect) {
        RectF newRect = new RectF();

        float srcLeft = srcRect.left;
        float srcTop = srcRect.top;
        float srcRight = srcRect.right;
        float srcBottom = srcRect.bottom;

        float dstLeft = (srcLeft - mScalePivotX) * mScale + mScalePivotX + mDistanceX;
        float dstTop = (srcTop - mScalePivotY) * mScale + mScalePivotY + mDistanceY;
        float dstRight = (srcRight - mScalePivotX) * mScale + mScalePivotX + mDistanceX;
        float dstBottom = (srcBottom - mScalePivotY) * mScale + mScalePivotY + mDistanceY;
        newRect.set(dstLeft, dstTop, dstRight, dstBottom);

        return newRect;
    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        mScaleFactor = scaleFactor;
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public float getLastScale() {
        return mLastScale;
    }

    public void setLastScale(float lastScale) {
        mLastScale = lastScale;
    }

    public float getScalePivotX() {
        return mScalePivotX;
    }

    public void setScalePivotX(float scalePivotX) {
        mScalePivotX = scalePivotX;
    }

    public float getScalePivotY() {
        return mScalePivotY;
    }

    public void setScalePivotY(float scalePivotY) {
        mScalePivotY = scalePivotY;
    }

    public float getDistanceX() {
        return mDistanceX;
    }

    public void setDistanceX(float distanceX) {
        mDistanceX = distanceX;
    }

    public float getDistanceY() {
        return mDistanceY;
    }

    public void setDistanceY(float distanceY) {
        mDistanceY = distanceY;
    }

    public float getLastDistanceX() {
        return mLastDistanceX;
    }

    public void setLastDistanceX(float lastDistanceX) {
        mLastDistanceX = lastDistanceX;
    }

    public float getLastDistanceY() {
        return mLastDistanceY;
    }

    public void setLastDistanceY(float lastDistanceY) {
        mLastDistanceY = lastDistanceY;
    }
}
