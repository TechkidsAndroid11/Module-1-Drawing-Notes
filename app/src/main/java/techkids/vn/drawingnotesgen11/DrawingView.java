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

    // có thể hiểu như 1 cái bảng/ 1 tờ giấy trắng để vẽ lên
    private Canvas canvas;
    // bút vẽ
    private Paint paint;
    // đường vẽ
    private Path path;
    // lưu lại từng nét vẽ sau mỗi lần nhấc bút
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

    // hàm đc gọi sau khi view thay đổi kích thước xong
    // ban đầu kích thước tất cả các view là 0, 0; qua 1 đoạn tính toán mới thay đổi sang kích thước mới
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);

        canvas = new Canvas(bitmap);
    }

    // đc gọi lần đầu khi view khởi tạo và sau mỗi lần gọi invalidate()
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

                // set lại màu + nét vẽ cho paint
                paint.setColor(DrawActivity.currentColor);
                paint.setStrokeWidth(DrawActivity.currentSize);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                path.lineTo(touchX, touchY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                //commit lại nét vẽ trước khi reset. nếu k nhấc tay ra nét vẽ sẽ biến mất
                canvas.drawPath(path, paint);
                //reset lại path, nếu k đến lúc đổi màu nó sẽ đổi màu tất cả các line
                path.reset();
                break;
            }
        }
        // có thể đc gọi cả bởi system hoặc user. gọi mỗi khi cần update view
        // vd: edittext đang ở trạng thái bt, bấm vào ra trạng thái khác -> do system gọi invalidate()
        invalidate();

        // return true để nhận event touch liên tục
        // return false chỉ nhận event lần đầu chạm tay xuống
        return true;
    }
}
