package com.example.optipark;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

//Custom View for drawing lines connecting the user's position to a selected parking space.
public class LineView extends View {

    // Variables
    private Paint paint;
    private float startX, startY, endX, endY;
    private float secondLineEndX, secondLineEndY;
    private boolean isKeyboardVisible = false;
    private float lastParkingSpaceHeight = 10; // Height of the last parking space image

    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Initialize the Paint object
    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(35);
        paint.setStrokeCap(Paint.Cap.ROUND); // Rounded line ends
        paint.setStrokeJoin(Paint.Join.ROUND); // Rounded line joints
        // To set a dashed line, uncomment the following line and adjust the dash interval and gap
        paint.setPathEffect(new DashPathEffect(new float[]{20, 80}, 0)); // 10 pixels on, 20 pixels off
    }


    // Override the onDraw method to draw the lines
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the first line (vertical line)
        canvas.drawLine(startX, startY, endX, endY, paint);

        // Draw the second line (horizontal line) only if the keyboard is not visible
        if (!isKeyboardVisible) {
            canvas.drawLine(endX, endY, secondLineEndX, secondLineEndY, paint);
        }

        Log.d("LineView", "startX: " + startX + ", startY: " + startY + ", endX: " + endX + ", endY: " + endY + ", secondLineEndX: " + secondLineEndX + ", secondLineEndY: " + secondLineEndY);
    }

    // Method to draw lines connecting the user's position to a selected parking space
    public void drawToParkingSpace(float x, float y) {
        startX = getWidth() / 2;
        startY = getHeight() - lastParkingSpaceHeight;
        endX = startX;
        endY = (y < startY) ? y : startY;

        float slope = (y - endY) / (x - endX);
        float horizontalDistance = (x > endX) ? getWidth() - endX : endX;
        float maxLength = getWidth() * 0.27f;
        float calculatedLength = (float) Math.sqrt(Math.pow(horizontalDistance, 2) + Math.pow(horizontalDistance * slope, 2));
        float clampedLength = Math.min(calculatedLength, maxLength);

        secondLineEndX = (x > endX) ? endX + clampedLength : endX - clampedLength;
        secondLineEndY = endY + (slope * clampedLength);

        invalidate();
    }
}
