package com.example.uberv.imagemasking;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
//        Bitmap out = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        Bitmap out = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        c.drawBitmap(bitmap, 0, 0, paint);

        Bitmap blackBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Paint black = new Paint();
        black.setColor(Color.BLACK);
        Canvas bc = new Canvas(blackBitmap);
        bc.drawRGB(0, 0, 0);

        Paint mergePaint = new Paint();
        PorterDuff.Mode mode = PorterDuff.Mode.DST_IN;
        mergePaint.setXfermode(new PorterDuffXfermode(mode));
        bc.drawBitmap(bitmap, 0, 0, mergePaint);
        // now we have black version of our bitmap
        // next - blur it

        // TODO resize black version (scale it up)

        // define this only once if blurring multiple times
        RenderScript rs = RenderScript.create(this);

        // this will blur the bitmapOriginal with a radius of 8 and save it in bitmapOriginal
        final Allocation input = Allocation.createFromBitmap(rs, blackBitmap); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(8f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(out);

        Bitmap tmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tmp);
        canvas.drawBitmap(out, 0, 0, new Paint());
        canvas.drawBitmap(bitmap, 0, 0, new Paint());

        // recycle unused bitmaps
        bitmap.recycle();
        blackBitmap.recycle();
        // TODO also set according config types (channels, etc)

        mImageView.setImageBitmap(tmp);

        Log.d("TAG", "DONE");
    }
}
