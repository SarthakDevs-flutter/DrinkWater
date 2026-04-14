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
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.trending.water.drinking.reminder.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BitmapHelper {
    private static final String TAG = "BitmapHelper";
    private final Context context;

    public BitmapHelper(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    public static Bitmap getResizedBitmap(@NonNull Bitmap bitmap, float newHeight, float newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / (float) width, newHeight / (float) height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    @NonNull
    public static Bitmap scaleDown(@NonNull Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / (float) realImage.getWidth(), maxImageSize / (float) realImage.getHeight());
        int width = Math.round(realImage.getWidth() * ratio);
        int height = Math.round(realImage.getHeight() * ratio);
        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    @Nullable
    public static String getFilePath(@NonNull Activity activity, @NonNull Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        Context appContext = activity.getApplicationContext();

        if (DocumentsContract.isDocumentUri(appContext, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
            } else if (isMediaDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            try (Cursor cursor = activity.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow("_data");
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file path from content URI", e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Nullable
    public Bitmap getBitmapFromDrawable(int drawableResId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableResId);
    }

    @Nullable
    public Bitmap getBitmapFromSdCard(@NonNull String imagePath) {
        return BitmapFactory.decodeFile(imagePath);
    }

    @Nullable
    public Bitmap getBitmapFromUrl(@NonNull String imageUrl) {
        try (InputStream is = new URL(imageUrl).openStream()) {
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e(TAG, "Error downloading bitmap", e);
            return null;
        }
    }

    @Nullable
    public Bitmap getBitmapFromBytes(@Nullable byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Nullable
    public byte[] getBytesFromBitmap(@Nullable Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    public boolean saveBitmap(@NonNull Bitmap bitmap, @NonNull String imageName) {
        File file = new File(Environment.getExternalStorageDirectory(), imageName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving bitmap", e);
            return false;
        }
    }

    public boolean saveBitmap(@NonNull Bitmap bitmap, @NonNull String imageName, @NonNull String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) return false;
        File file = new File(dir, imageName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving bitmap to path", e);
            return false;
        }
    }

    public boolean saveBytes(@Nullable byte[] bytes, @NonNull String imageName, @NonNull String path) {
        Bitmap bitmap = getBitmapFromBytes(bytes);
        return bitmap != null && saveBitmap(bitmap, imageName, path);
    }

    @NonNull
    public Drawable setTint(@NonNull Drawable drawable, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    @Nullable
    public Uri getImageUri(@NonNull Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return path != null ? Uri.parse(path) : null;
    }

    @Nullable
    public String getPath(@NonNull Uri uri) {
        try (Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow("_data");
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting path from Uri", e);
        }
        return null;
    }

    @NonNull
    public Bitmap getBitmapFromScrollView(@NonNull ScrollView scrollView) {
        int height = 0;
        int whiteColor = ContextCompat.getColor(context, R.color.white);
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            height += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(whiteColor);
        }
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), height, Bitmap.Config.ARGB_8888);
        scrollView.draw(new Canvas(bitmap));
        return bitmap;
    }

    @Nullable
    public byte[] convertToByteArray(@NonNull InputStream inputStream) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "Error converting InputStream to byte array", e);
            return null;
        }
    }

    @Nullable
    public Bitmap resizeMapIcons(@NonNull String iconName, int width, int height) {
        int resId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        if (resId == 0) return null;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if (bitmap == null) return null;
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    @Nullable
    public String getFileName(@NonNull Uri uri) {
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        return cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file name from content Uri", e);
            }
        }

        String path = uri.getPath();
        if (path != null) {
            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                return path.substring(cut + 1);
            }
            return path;
        }
        return null;
    }
}
