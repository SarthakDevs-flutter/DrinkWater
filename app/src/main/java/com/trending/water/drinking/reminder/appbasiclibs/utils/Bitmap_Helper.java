package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ScrollView;

import androidx.core.graphics.drawable.DrawableCompat;

import com.trending.water.drinking.reminder.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class Bitmap_Helper {
    Bitmap bitmap = null;
    byte[] bytes;
    Context mContext;

    public Bitmap_Helper(Context mContext2) {
        this.mContext = mContext2;
    }

    public static Bitmap ResizedBitmap(Bitmap bm, float newHeight, float newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / ((float) width), newHeight / ((float) height));
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / ((float) realImage.getWidth()), maxImageSize / ((float) realImage.getHeight()));
        return Bitmap.createScaledBitmap(realImage, Math.round(((float) realImage.getWidth()) * ratio), Math.round(((float) realImage.getHeight()) * ratio), filter);
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getFilePath(Activity act, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(act.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue());
            } else if (isMediaDocument(uri)) {
                String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                String type = split2[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split2[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            try {
                Cursor cursor = act.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, (String) null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

//    public void PrintPhoto(Bitmap bitmap2) {
//        PrintHelper photoprinter = new PrintHelper(this.mContext);
//        photoprinter.setScaleMode(1);
//        photoprinter.printBitmap("Print", bitmap2);
//    }

    public Bitmap bitmap_from_drawable(int drawable) {
        this.bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), drawable);
        return this.bitmap;
    }

    public Bitmap bitmap_from_sdcard(String image_path) {
        this.bitmap = BitmapFactory.decodeFile(image_path);
        return this.bitmap;
    }

    public Bitmap bitmap_from_url(String image_url) {
        try {
            this.bitmap = BitmapFactory.decodeStream((InputStream) new URL(image_url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.bitmap;
    }

    public Bitmap bitmap_from_bytes(byte[] b) {
        this.bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return this.bitmap;
    }

    public byte[] bytes_from_bitmap(Bitmap bitmap2) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, baos);
        this.bytes = baos.toByteArray();
        return this.bytes;
    }

    public boolean saveBitmap(Bitmap bitmap2, String image_name) {
        try {
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + image_name);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e2) {
            return false;
        } catch (Exception e3) {
            return false;
        }
    }

    public boolean saveBitmap(Bitmap bitmap2, String image_name, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path + "/" + image_name);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e2) {
            return false;
        } catch (Exception e3) {
            return false;
        }
    }

    public boolean saveBytes(byte[] b, String image_name, String path) {
        return saveBitmap(bitmap_from_bytes(b), image_name, path);
    }

    public boolean saveBytes(byte[] b, String image_name) {
        return saveBitmap(bitmap_from_bytes(b), image_name);
    }

    public Drawable setTint(Drawable drawable, int color) {
        Drawable newDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(newDrawable, color);
        return newDrawable;
    }

    public Uri getImageUri(Bitmap inImage) {
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(this.mContext.getContentResolver(), inImage, "Title", (String) null));
    }

    public String getPath(Uri uri) {
        Cursor cursor = this.mContext.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

//    public long getFileSize(String imagePath) {
//        long length = 0;
//        try {
//            length = new File(imagePath).length();
//            return length / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return length;
//        }
//    }

    public Bitmap getBitmapFromScrollView(ScrollView scrollView) {
        int h = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(R.color.white);
        }
        Bitmap bitmap2 = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
        scrollView.draw(new Canvas(bitmap2));
        return bitmap2;
    }

    public byte[] convertToByteArray(InputStream inputStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int next = inputStream.read();
            while (next > -1) {
                bos.write(next);
                next = inputStream.read();
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = this.mContext.getContentResolver().query(contentUri, (String[]) null, (String) null, (String[]) null, (String) null);
        if (cursor == null) {
            return contentUri.getPath();
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("_data"));
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), this.mContext.getResources().getIdentifier(iconName, "drawable", this.mContext.getPackageName())), width, height, false);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.mContext.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex("_display_name"));
                    }
                } catch (Throwable th) {
                    cursor.close();
                    throw th;
                }
            }
            cursor.close();
        }
        if (result != null) {
            return result;
        }
        String result2 = uri.getPath();
        int cut = result2.lastIndexOf(47);
        if (cut != -1) {
            return result2.substring(cut + 1);
        }
        return result2;
    }
}
