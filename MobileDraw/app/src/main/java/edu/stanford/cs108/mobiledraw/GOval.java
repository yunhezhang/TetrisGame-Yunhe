package edu.stanford.cs108.mobiledraw;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Yunhe Zhang on 2/21/2017.
 */

public class GOval extends GObject {
    private String shape;
    public GOval(float inputX, float inputY, float inputRightX, float inputBottomY) {
        super(inputX, inputY, inputRightX, inputBottomY);
        this.shape = "Oval";
    }

    public String getShape() {
        return this.shape;
    }

    @Override
    public void drawSelf(Canvas canvas) {
        canvas.drawOval(new RectF(this.getX(),this.getY(),this.getRightX(),this.getBottomY()),
                         this.getPaint());
    }
}
