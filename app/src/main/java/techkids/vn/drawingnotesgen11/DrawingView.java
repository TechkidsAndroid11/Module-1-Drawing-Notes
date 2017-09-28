package techkids.vn.drawingnotesgen11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Admins on 9/27/2017.
 */

public class DrawingView extends View {
    private static final String TAG = DrawingView.class.toString();

    private Canvas canvas;
    private Paint paint;
    private Path path;
    private Bitmap bitmap;

    public DrawingView(Context context) {
        super(context);

        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        Log.d(TAG, "DrawingView: ");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
        Log.d(TAG, "onDraw: ");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                path.moveTo(touchX, touchY);

                paint.setColor(DrawActivity.currentColor);
                paint.setStrokeWidth(DrawActivity.currentSize);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                path.lineTo(touchX, touchY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                canvas.drawPath(path, paint);
                path.reset();
                break;
            }
        }

        invalidate();
        return true;
    }
}
