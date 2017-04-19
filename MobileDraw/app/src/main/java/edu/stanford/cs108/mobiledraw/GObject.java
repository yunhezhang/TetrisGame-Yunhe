package edu.stanford.cs108.mobiledraw;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Yunhe Zhang on 2/21/2017.
 */

public class GObject {
    private float x;
    private float y;
    private float rightX;
    private float bottomY;
    private Paint paint;

    public GObject(float inputX, float inputY, float inputRightX, float inputBottomY) {
        this.x = inputX;
        this.y = inputY;
        this.rightX = inputRightX;
        this.bottomY = inputBottomY;
        this.paint = new Paint();
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public float getRightX() {
        return rightX;
    }

    public float getBottomY() {
        return bottomY;
    }

    public void setPaint(Paint inputPaint) {
        this.paint = inputPaint;
    }

    public void drawSelf(Canvas canvas) {
        return;
    }
}
