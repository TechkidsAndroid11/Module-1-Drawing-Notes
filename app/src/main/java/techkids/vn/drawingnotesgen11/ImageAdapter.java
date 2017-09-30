package techkids.vn.drawingnotesgen11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admins on 9/30/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private final String TAG = ImageAdapter.class.toString();

    List<String> imagePaths = new ArrayList<>();
    Context context;

    public ImageAdapter(List<String> imagePaths, Context context) {
        this.imagePaths = imagePaths;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);

        //set size
        imageView.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setCropToPadding(true);

        //set data
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePaths.get(i), options);
        imageView.setImageBitmap(bitmap);

        return imageView;
    }
}
