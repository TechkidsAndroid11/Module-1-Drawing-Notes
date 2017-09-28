package techkids.vn.drawingnotesgen11;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class DrawActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DrawActivity.class.toString();
    private ImageView ivPickColor;
    private ImageView ivSave;
    private RadioGroup radioGroup;
    private DrawingView drawingView;

    public static int currentColor = 0xfff7a72e;
    public static int currentSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        setupUI();
        addDrawingView();

        addListeners();
    }

    private void addDrawingView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_drawing);

        drawingView = new DrawingView(this);
        drawingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        relativeLayout.addView(drawingView);
    }

    private void addListeners() {
        ivPickColor.setOnClickListener(this);
        ivSave.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_thin: {
                        currentSize = 5;
                        break;
                    }
                    case R.id.rb_med: {
                        currentSize = 10;
                        break;
                    }
                    case R.id.rb_strong: {
                        currentSize = 15;
                        break;
                    }
                }
                Log.d(TAG, "onCheckedChanged: " + currentSize);
            }
        });
    }

    private void setupUI() {
        ivPickColor = (ImageView) findViewById(R.id.iv_pick_color);
        ivPickColor.setColorFilter(currentColor);

        ivSave = (ImageView) findViewById(R.id.iv_save);

        radioGroup = (RadioGroup) findViewById(R.id.gr_pen_size);
        radioGroup.check(R.id.rb_med);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pick_color: {
                pickColor();
                break;
            }
            case R.id.iv_save: {
                saveImage();
                break;
            }
        }
    }

    private void saveImage() {
        drawingView.setDrawingCacheEnabled(true);
        drawingView.buildDrawingCache();
        Bitmap bitmap = drawingView.getDrawingCache();

        Log.d(TAG, "saveImage: " + bitmap.getWidth());

        ImageUtils.saveImage(bitmap, this);
    }

    private void pickColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose your color")
                .initialColor(currentColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                        ivPickColor.setColorFilter(i);
                        currentColor = i;
                    }
                })
                .lightnessSliderOnly()
                .build()
                .show();
    }
}
