package techkids.vn.drawingnotesgen11;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.toString();
    private FloatingActionButton fbNewNote;
    private SubActionButton btCameraNote;
    private SubActionButton btBlankNote;
    private FloatingActionMenu floatingActionMenu;
    private GridView gvImage;

    public static String MODE_CAMERA = "mode_camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
        setupUI();
        addListeners();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {

        }
    }

    private void addListeners() {
        btCameraNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: camera");
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                intent.putExtra(MODE_CAMERA, true);
                startActivity(intent);
                floatingActionMenu.close(false);
            }
        });

        btBlankNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);
                intent.putExtra(MODE_CAMERA, false);
                startActivity(intent);
                floatingActionMenu.close(false);
            }
        });
    }

    private void setupUI() {
        fbNewNote = (FloatingActionButton) findViewById(R.id.fb_new_note);

        SubActionButton.Builder sabBuilder = new SubActionButton.Builder(this);
        btCameraNote = sabBuilder.setBackgroundDrawable
                (getResources().getDrawable(R.drawable.ic_camera_alt_black_24dp))
                .build();
        btBlankNote = sabBuilder.setBackgroundDrawable
                (getResources().getDrawable(R.drawable.ic_brush_black_24dp))
                .build();

        floatingActionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btCameraNote)
                .addSubActionView(btBlankNote)
                .attachTo(fbNewNote)
                .build();

        gvImage = (GridView) findViewById(R.id.gv_images);

    }

    private List<String> getListImagePath() {
        List<String> imagePaths = new ArrayList<>();

        File imageFolder = new File(Environment.getExternalStorageDirectory().toString()
                + "/DrawingNotes");
        File[] listFile = imageFolder.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                String filePath = listFile[i].getAbsolutePath();
                imagePaths.add(filePath);
            }
        }

        return imagePaths;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageAdapter imageAdapter = new ImageAdapter(getListImagePath(), this);
        gvImage.setAdapter(imageAdapter);
    }
}
