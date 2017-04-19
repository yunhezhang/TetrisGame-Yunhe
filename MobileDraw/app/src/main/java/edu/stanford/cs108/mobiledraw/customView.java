package edu.stanford.cs108.mobiledraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yunhe Zhang on 2/21/2017.
 */

public class customView extends View implements View.OnClickListener {
    private List<GObject> list;
    private RadioGroup radioGroup;
    private Paint redOutlinePaint;
    private Paint whiteFillPaint;
    private Paint blueOutlinePaint;



    public customView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    private void init() {
        list = new ArrayList<>();
        radioGroup = (RadioGroup)((Activity) getContext()).findViewById(R.id.modeChoice);
        redOutlinePaint = new Paint();
        redOutlinePaint.setColor(Color.RED);
        redOutlinePaint.setStyle(Paint.Style.STROKE);
        redOutlinePaint.setStrokeWidth(5.0f);
        whiteFillPaint = new Paint();
        whiteFillPaint.setColor(Color.WHITE);
        blueOutlinePaint = new Paint();
        blueOutlinePaint.setColor(Color.BLUE);
        blueOutlinePaint.setStyle(Paint.Style.STROKE);
        blueOutlinePaint.setStrokeWidth(15.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        EditText xText = (EditText) ((Activity) getContext()).findViewById(R.id.inputX);
        EditText yText = (EditText) ((Activity) getContext()).findViewById(R.id.inputY);
        EditText wText = (EditText) ((Activity) getContext()).findViewById(R.id.inputWidth);
        EditText hText = (EditText) ((Activity) getContext()).findViewById(R.id.inputHeight);
        super.onDraw(canvas);
        radioGroup = (RadioGroup)((Activity) getContext()).findViewById(R.id.modeChoice);
        int currentCheck = radioGroup.getCheckedRadioButtonId();
        for(int i = 0; i < list.size(); i++) {
            if(currentCheck == R.id.Oval || currentCheck == R.id.Rect) {
                if (i != list.size() - 1 && list.get(i).getPaint() == blueOutlinePaint) {
                    list.get(i).setPaint(redOutlinePaint);
                    list.get(i).drawSelf(canvas);
                } else if (i == list.size() - 1) {
                    list.get(i).setPaint(blueOutlinePaint);
                    xText.setText(String.valueOf(list.get(i).getX()));
                    yText.setText(String.valueOf(list.get(i).getY()));
                    wText.setText(String.valueOf(list.get(i).getRightX()-list.get(i).getX()));
                    hText.setText(String.valueOf(list.get(i).getBottomY()-list.get(i).getY()));
                    list.get(i).drawSelf(canvas);
                }
                list.get(i).drawSelf(canvas);
            }
            else if(currentCheck == R.id.Select || currentCheck == R.id.Erase) {
                list.get(i).drawSelf(canvas);
            }
        }
    }
    float x1,y1,x2,y2;
    float left,right,top,bottom;
    float selectX, selectY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        radioGroup = (RadioGroup)((Activity) getContext()).findViewById(R.id.modeChoice);
        Button b = (Button)((Activity) getContext()).findViewById(R.id.update);
        int currentCheck = radioGroup.getCheckedRadioButtonId();
        if(currentCheck == R.id.Oval || currentCheck == R.id.Rect) {
              modeRecOval(currentCheck, event);
        }
        if(currentCheck == R.id.Select) {
              modeSelect(event);
        }
        if(currentCheck == R.id.Erase) {
            modeErase(event);
        }
        return true;
    }

    private void modeSelect(MotionEvent event) {
        EditText xText = (EditText) ((Activity) getContext()).findViewById(R.id.inputX);
        EditText yText = (EditText) ((Activity) getContext()).findViewById(R.id.inputY);
        EditText wText = (EditText) ((Activity) getContext()).findViewById(R.id.inputWidth);
        EditText hText = (EditText) ((Activity) getContext()).findViewById(R.id.inputHeight);
        float currentLeft;
        float currentRight;
        float currentTop;
        float currentBottom;
        boolean status = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectX = event.getX();
                selectY = event.getY();
                for(int i = list.size()-1; i >=0; i--) {
                    currentLeft = list.get(i).getX();
                    currentRight = list.get(i).getRightX();
                    currentTop = list.get(i).getY();
                    currentBottom = list.get(i).getBottomY();
                    if (list.get(i).getPaint() == blueOutlinePaint) {
                        if (selectX < currentLeft || selectX > currentRight ||
                                selectY < currentTop || selectY > currentBottom || status == true){
                            list.get(i).setPaint(redOutlinePaint);
                            continue;
                        }
                    }
                    else if (list.get(i).getPaint() == redOutlinePaint &&
                            selectX >= currentLeft && selectX <= currentRight &&
                            selectY >= currentTop && selectY <= currentBottom) {
                        for(int j = i+1; j < list.size(); j++) {
                            if(list.get(j).getPaint() == blueOutlinePaint) status = true;
                        }
                        if (status == false) {
                            list.get(i).setPaint(blueOutlinePaint);
                            xText.setText(String.valueOf(list.get(i).getX()));
                            yText.setText(String.valueOf(list.get(i).getY()));
                            wText.setText(String.valueOf(list.get(i).getRightX()-list.get(i).getX()));
                            hText.setText(String.valueOf(list.get(i).getBottomY()-list.get(i).getY()));
                            status = true;
                        }
                    }
                }
                if(status == false) {
                    xText.setText("");
                    yText.setText("");
                    wText.setText("");
                    hText.setText("");
                }
                status = false;
                invalidate();
        }
    }

    private void modeErase(MotionEvent event) {
        float currentLeft;
        float currentRight;
        float currentTop;
        float currentBottom;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectX = event.getX();
                selectY = event.getY();
                for (int i = list.size() - 1; i >= 0; i--) {
                    currentLeft = list.get(i).getX();
                    currentRight = list.get(i).getRightX();
                    currentTop = list.get(i).getY();
                    currentBottom = list.get(i).getBottomY();
                    if((list.get(i).getPaint()==redOutlinePaint ||
                            list.get(i).getPaint() == blueOutlinePaint) &&
                    selectX >= currentLeft && selectX <= currentRight &&
                            selectY >= currentTop && selectY <= currentBottom) {
                        list.remove(i);
                        list.remove(i - 1);
                        i--;
                        break;
                    }
                }
                invalidate();
        }
    }

    private void modeRecOval(int currentCheck, MotionEvent event) {
        GObject newObject = null;
        GObject newObject2 = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                if (x1>x2) {
                    left = x2;
                    right = x1;
                } else {
                    left = x1;
                    right = x2;
                }
                if (y1>y2) {
                    top = y2;
                    bottom = y1;
                } else {
                    top = y1;
                    bottom = y2;
                }
                if(currentCheck == R.id.Oval) {
                    newObject = new GOval(left, top, right, bottom);
                    newObject2 = new GOval(left, top, right, bottom);
                }
                else if(currentCheck == R.id.Rect) {
                    newObject = new GRect(left, top, right, bottom);
                    newObject2 = new GRect(left, top, right, bottom);
                }
                newObject.setPaint(whiteFillPaint);
                list.add(newObject);
                newObject2.setPaint(redOutlinePaint);
                list.add(newObject2);
                invalidate();
        }
    }

    public void onClick(View view) {
        EditText xText = (EditText) ((Activity) getContext()).findViewById(R.id.inputX);
        EditText yText = (EditText) ((Activity) getContext()).findViewById(R.id.inputY);
        EditText wText = (EditText) ((Activity) getContext()).findViewById(R.id.inputWidth);
        EditText hText = (EditText) ((Activity) getContext()).findViewById(R.id.inputHeight);
        float x = Float.parseFloat(xText.getText().toString());
        float y = Float.parseFloat(yText.getText().toString());
        float w = Float.parseFloat(wText.getText().toString());
        float h = Float.parseFloat(hText.getText().toString());
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getPaint() == redOutlinePaint ||
                    list.get(i).getPaint() == blueOutlinePaint) {
                if (list.get(i).getX() == x && list.get(i).getY() == y &&
                        list.get(i).getRightX() - list.get(i).getX() == w &&
                        list.get(i).getBottomY() - list.get(i).getY() == h) {
                    list.get(i).setPaint(blueOutlinePaint);
                } else list.get(i).setPaint(redOutlinePaint);
            }
        }
        invalidate();
    }
}
