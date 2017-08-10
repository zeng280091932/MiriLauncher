/* 
 * 文件名：IconUtil.java
 */
package com.miri.launcher.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @author penglin
 * @version 2013-6-6
 */
public class IconUtil {

    public static Bitmap createIconBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                .getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] drawableToByte(Drawable drawable) {
        Bitmap bitmap = createIconBitmap(drawable);
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        // 创建一个字节数组输出流,流的大小为size  
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中  
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        // 将字节数组输出流转化为字节数组byte[]  
        byte[] imagedata = baos.toByteArray();
        return imagedata;
    }

    public static Drawable byteToDrawable(byte[] data) {
        Bitmap bitmap;
        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Drawable drawable = new BitmapDrawable(bitmap);
            return drawable;
        }
        return null;
    }

    public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);//除了PNG还有很多常见格式，如jpeg等。
        return os.toByteArray();
    }

    public static Bitmap bytesToBitmap(byte[] data) {
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitmap;
        }
        return null;

    }

    public static byte[] flattenBitmap(Bitmap bitmap) {
        // Try go guesstimate how much space the icon will take when serialized
        // to avoid unnecessary allocations/copies during the write.
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            Logger.getLogger().w("Could not write icon");
            return null;
        }
    }

    public static byte[] flattenDrawable(Drawable drawable) {
        Bitmap bitmap = createIconBitmap(drawable);
        return flattenBitmap(bitmap);
    }

    public static Drawable getShortcutIncoResource(Context context, ShortcutIconResource iconRes) {
        Drawable shortcutIcon = null;
        //获得inconRes对象的Resource对象  
        try {
            //获取对应packageName的Resources对象  
            Resources resources = context.getPackageManager().getResourcesForApplication(
                    iconRes.packageName);
            //获取对应图片的id号  
            int iconid = resources.getIdentifier(iconRes.resourceName, null, null);
            //            Logger.getLogger().d("icon identifier is " + iconRes.resourceName);
            //获取资源图片  
            shortcutIcon = resources.getDrawable(iconid);
        } catch (NameNotFoundException e) {
            Logger.getLogger().e("NameNotFoundException  at completeAddShortCut method");
        } catch (NotFoundException e) {
            Logger.getLogger().e("NotFoundException  at completeAddShortCut method");
        }
        return shortcutIcon;
    }

    /**
     * 将图片像素点都改成白色
     * @param bitmap
     * @return
     */
    public static Bitmap changeColorFFFFFF(Bitmap mBitmap) {
        //从文件中读取的图片是不可被改变的，不能直接使用setPixel()来设置他的像素点颜色，把图片变成可改变的.
        Bitmap bitmap = mBitmap.copy(mBitmap.getConfig(), true);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        for (int i = 0; i < bitmapHeight; i++) {
            for (int j = 0; j < bitmapWidth; j++) {
                //获得Bitmap 图片中每一个点的color颜色值  
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0  
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000   
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了  
                int color = bitmap.getPixel(j, i);
                if (color != 0) {
                    bitmap.setPixel(j, i, Color.WHITE);
                }
            }
        }
        return bitmap;
    }

    /**
     * 构造倒影图片
     * @param originalBitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createReflectedImage(Bitmap originalBitmap, int width, int height) {
        int i = originalBitmap.getWidth();
        int j = originalBitmap.getHeight();
        Matrix matrix = new Matrix();
        // 图片缩放，x轴变为原来的1倍，y轴为-1倍,实现图片的反转
        matrix.preScale(1, -1);
        // 创建反转后的图片Bitmap对象，图片高是原图的一半。
        int y = (j - height) < 0 ? 0 : (j - height);
        Bitmap reflectionBitmap = Bitmap.createBitmap(originalBitmap, 0, y, width, height, matrix,
                false);
        // 创建标准的Bitmap对象，宽和原图一致，高是原图的1.5倍。
        Bitmap withReflectionBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        // 构造函数传入Bitmap对象，为了在图片上画图
        Canvas canvas = new Canvas(withReflectionBitmap);

        // 画倒影图片
        canvas.drawBitmap(reflectionBitmap, 0, 0, null);

        // 实现倒影效果
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, withReflectionBitmap.getHeight(),
                0x40ffffff, 0x00ffffff, TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

        // 覆盖效果
        canvas.drawRect(0, 0, width, height, paint);

        return withReflectionBitmap;
    }

}
