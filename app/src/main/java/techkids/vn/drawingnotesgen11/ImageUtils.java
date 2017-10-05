package techkids.vn.drawingnotesgen11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
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
    private static File tempFile;

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

    public static Uri getUriFromImage(Context context) {
        //create temp file
        tempFile = null;
        try {
             tempFile = File.createTempFile(
                    Calendar.getInstance().getTime().toString(),
                    ".jpg",
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );

            Log.d(TAG, "getUriFromImage: " + tempFile.getPath());
            tempFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get uri
        Uri uri = null;
        if (tempFile != null) {
            uri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    tempFile
            );
        }
        Log.d(TAG, "getUriFromImage: " + uri);
        return uri;
    }

    public static Bitmap getBitmap(Context context) {
        // get bitmap from uri
        Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
        Log.d(TAG, "getBitmap: "+bitmap.getWidth());

        // scale
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        double ratio = (double) bitmap.getWidth() / bitmap.getHeight();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                screenWidth, (int) (screenWidth/ratio), false);

        return scaledBitmap;
    }
}
