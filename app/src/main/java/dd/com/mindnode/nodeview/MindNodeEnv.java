package dd.com.mindnode.nodeview;

import android.graphics.RectF;

import org.json.JSONObject;

import dd.com.mindnode.App;
import dd.com.mindnode.Utils;

public class MindNodeEnv {
    public static final float MAX_SCALE = 3;
    public static final float MIN_SCALE = 0.5f;


    private float mScaleFactor = 1;
    private float mScale = 1;
    private float mLastScale = 1;
    private float mScalePivotX;
    private float mScalePivotY;
    private float mDistanceX;
    private float mDistanceY;
    private float mLastDistanceX;
    private float mLastDistanceY;

    private ColorStyle mCurrentColorStyle;

    private static MindNodeEnv mindNodeEnv;

    private MindNodeEnv() {

        JSONObject jsonObject = Utils.getJsonObject("color_style_1.json", App.getAppContext());
        mCurrentColorStyle = new ColorStyle(jsonObject);
    }

    public static MindNodeEnv getEnv() {
        if (mindNodeEnv == null) {
            mindNodeEnv = new MindNodeEnv();
        }
        return mindNodeEnv;
    }

    /**
     * 双指放大缩小、单指空白区域平移后会导致canvas发生scale和translate，导致NodeView的肉眼坐标实际已经变化了。
     *
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
        mScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
    }

    public float getLastScale() {
        return mLastScale;
    }

    public void setLastScale(float lastScale) {
        mLastScale = Math.max(MIN_SCALE, Math.min(MAX_SCALE, lastScale));
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


    public ColorStyle getCurrentColorStyle() {
        return mCurrentColorStyle;
    }

    public void setCurrentColorStyle(ColorStyle currentColorStyle) {
        mCurrentColorStyle = currentColorStyle;
    }
}
