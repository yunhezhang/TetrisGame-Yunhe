package edu.stanford.cs108.mobiledraw;

import android.graphics.Canvas;

/**
 * Created by Yunhe Zhang on 2/21/2017.
 */

public class GRect extends GObject {
    private String shape;
    public GRect(float inputX, float inputY, float inputRightX, float inputBottomY) {
        super(inputX, inputY, inputRightX, inputBottomY);
        this.shape = "Rect";
    }

    public String getShape() {
        return shape;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        canvas.drawRect(this.getX(),this.getY(),this.getRightX(),
                this.getBottomY(), this.getPaint());
    }
}
