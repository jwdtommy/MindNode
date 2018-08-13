package dd.com.mindnode.nodeview;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

public class ColorStyle {
    public int bgColor;
    public int mainNodeBorderColor;
    public int mainNodeBgColor;
    public int textColor;
    public int[] lineColors;

    public ColorStyle(JSONObject jsonObject) {
        bgColor = Color.parseColor(jsonObject.optString("bgColor"));
        mainNodeBorderColor = Color.parseColor(jsonObject.optString("mainNodeBorderColor"));
        mainNodeBgColor = Color.parseColor(jsonObject.optString("mainNodeBgColor"));
        textColor = Color.parseColor(jsonObject.optString("textColor"));
        JSONArray jsonArray = jsonObject.optJSONArray("lineColors");
        lineColors = new int[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            lineColors[i] = Color.parseColor(jsonArray.optString(i));
        }
    }

}
