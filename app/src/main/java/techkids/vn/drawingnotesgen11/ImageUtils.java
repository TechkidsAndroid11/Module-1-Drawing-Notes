package techkids.vn.drawingnotesgen11;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Admins on 9/27/2017.
 */

public class ImageUtils {
    private static String TAG = ImageUtils.class.toString();

    // lưu image vào 1 folder riêng (folder DrawingNotes)
    public static void saveImage(Bitmap bitmap, Context context) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myFolder = new File(root + "/DrawingNotes");
        myFolder.mkdirs();

        String imageName = Calendar.getInstance().getTime().toString() + ".png";
        Log.d(TAG, "saveImage: " + imageName);

        File imageFile = new File(myFolder.toString(), imageName);

        try {
            FileOutputStream fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();

            //hiện thông báo sau khi save xong
            Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();

            //scan lai gallery de hien thi cac file moi
            MediaScannerConnection.scanFile(context,
                    new String[]{imageFile.getAbsolutePath()},
                    null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
