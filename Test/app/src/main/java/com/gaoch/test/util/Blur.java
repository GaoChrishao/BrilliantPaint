package com.gaoch.test.util;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class Blur {
    public static Bitmap bkg;
    static float BITMAP_SCALE=  0.2f;  //缩小比例
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }



    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void blur(View fromView, View toView, int radius, float scaleFactor,float roundCorner) {
        // 获取View的截图

        if (radius < 1 || radius > 26) {
            scaleFactor = 8;
            radius = 2;
        }

       if(bkg==null){
          initBkg(fromView,radius,scaleFactor);
       }


        int top,left,right,bottom;
        int[] location=new int[2];
        toView.getLocationInWindow(location);
        left=location[0];
        top=location[1];
        if(toView.getWidth()>0&&toView.getHeight()>0){
            Bitmap overlay = Bitmap.createBitmap((int) (toView.getWidth() / scaleFactor), (int) (toView.getHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(-left / scaleFactor, -top / scaleFactor);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);
            RoundedBitmapDrawable bdr=RoundedBitmapDrawableFactory.create(Resources.getSystem(),overlay);
            bdr.setCornerRadius(roundCorner);
            toView.setBackground(bdr);
        }




    }
    public static void blur_static(View fromView, View toView, int radius, float scaleFactor,float roundCorner) {
        // 获取View的截图
//        fromView.buildDrawingCache();
        if (radius < 1 || radius > 26) {
            scaleFactor = 8;
            radius = 2;
        }
        if(bkg==null){
            initBkg(fromView,radius,scaleFactor);
        }
        int top,left,right,bottom;
        int[] location=new int[2];
        toView.getLocationInWindow(location);
        left=location[0];
        top=location[1];
        if(toView.getWidth()>0&&toView.getHeight()>0){
            Bitmap overlay = Bitmap.createBitmap(
                    (int) (toView.getWidth() / scaleFactor),
                    (int) (toView.getHeight() / scaleFactor),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(-left / scaleFactor, -top / scaleFactor);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(bkg, 0, 0, paint);
            //overlay = doBlur(overlay, (int) radius, true);
            RoundedBitmapDrawable bdr=RoundedBitmapDrawableFactory.create(Resources.getSystem(),overlay);
            bdr.setCornerRadius(roundCorner);
            toView.setBackground(bdr);
        }
    }

    static void initBkg(View fromView,int radius, float scaleFactor){
        BitmapDrawable bd=(BitmapDrawable) fromView.getBackground();
        Bitmap bkg1=bd.getBitmap();
        fromView.destroyDrawingCache();
        fromView.setDrawingCacheEnabled(true);
        int height = (int) (fromView.getDrawingCache().getHeight());  //屏幕高度
        int width = (int) (fromView.getDrawingCache().getWidth());    //屏幕宽度

        //精确缩放到指定大小
        Bitmap bkg_origin= Bitmap.createScaledBitmap(bkg1,(int)(width/scaleFactor),(int)(height/scaleFactor), true);
        bkg_origin.setConfig(Bitmap.Config.ARGB_8888);
        bkg=Bitmap.createScaledBitmap(doBlur(bkg_origin,radius,true),width,height,true);
        Log.e("Blur","高斯模糊背景图片");
    }

    public static void initBkg(Bitmap bitmap,int width,int height){
        if(bitmap!=null){
            //bitmap.setConfig(Bitmap.Config.ARGB_8888);
            bkg=Bitmap.createScaledBitmap(bitmap,width,height,true);
            Log.e("GGG","加载毛玻璃图片!"+bkg.getWidth()+" "+bkg.getHeight());
        }
    }


    static void saveBitmap(final String bitName,final Bitmap mBitmap) {
        new Runnable() {
            @Override
            public void run() {
                File f = new File("" + bitName + ".png");
                try {
                    f.createNewFile();
                } catch (IOException e) {
                }
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                try {
                    fOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.run();

    }
}
