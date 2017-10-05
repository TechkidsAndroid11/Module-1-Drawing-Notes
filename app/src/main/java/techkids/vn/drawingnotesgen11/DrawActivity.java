package techkids.vn.drawingnotesgen11;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

        if (getIntent().getBooleanExtra(MainActivity.MODE_CAMERA, false)) {
            openCamera();
            Log.d(TAG, "onCreate: openCamera");
        } else {
            addDrawingView(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, null);
            Log.d(TAG, "onCreate: addDrawingView");
        }
        addListeners();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri uri = ImageUtils.getUriFromImage(this);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = ImageUtils.getBitmap(this);
                addDrawingView(bitmap.getWidth(), bitmap.getHeight(), bitmap);
            }
        }
    }

    // thêm view vào = code. lý do của việc add = code mà k phải = xml là vì chưa biết trước kích thước view
    // (vẽ lên background trắng -> size full màn hình, vẽ lên ảnh -> size = size ảnh)
    private void addDrawingView(int width, int height, Bitmap bitmap) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_drawing);

        drawingView = new DrawingView(this, bitmap);
        // set kích thước cho view
        drawingView.setLayoutParams
                (new ViewGroup.LayoutParams(width, height));
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
                ivSave.setClickable(false);
                this.finish();
                break;
            }
        }
    }

    private void saveImage() {
        // lấy ra bitmap chứa tất cả những gì đã vẽ lên view
        drawingView.setDrawingCacheEnabled(true);
        drawingView.buildDrawingCache();
        Bitmap bitmap = drawingView.getDrawingCache();

        Log.d(TAG, "saveImage: " + bitmap.getWidth());

        // lưu vào gallery
        ImageUtils.saveImage(bitmap, this);
    }

    private void pickColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose your color")
                .initialColor(currentColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                //density càng cao -> càng nhiều màu
                .density(12)
                .setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                        ivPickColor.setColorFilter(i);
                        currentColor = i;
                    }
                })
                // chỉ cho user chỉnh sáng tối, k cho chỉnh đậm nhạt
                .lightnessSliderOnly()
                .build()
                .show();
    }
}
