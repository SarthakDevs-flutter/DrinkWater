package com.trending.water.drinking.reminder.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.Comparator;

public class FileUtils {
    private static final String TAG = "FileUtils";
    
    public static final String HIDDEN_PREFIX = ".";
    public static final String MIME_TYPE_APP = "application/*";
    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_VIDEO = "video/*";

    public static final Comparator<File> FILE_COMPARATOR = (f1, f2) -> 
            f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());

    public static final FileFilter DIRECTORY_FILTER = file -> 
            file.isDirectory() && !file.getName().startsWith(HIDDEN_PREFIX);

    public static final FileFilter FILE_FILTER = file -> 
            file.isFile() && !file.getName().startsWith(HIDDEN_PREFIX);

    private FileUtils() {
        // Private constructor to prevent instantiation
    }

    @Nullable
    public static String getExtension(@Nullable String uri) {
        if (uri == null) return null;
        int dot = uri.lastIndexOf(".");
        if (dot >= 0) return uri.substring(dot);
        return "";
    }

    public static boolean isLocal(@Nullable String url) {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://");
    }

    public static boolean isMediaUri(@Nullable Uri uri) {
        return uri != null && "media".equalsIgnoreCase(uri.getAuthority());
    }

    @Nullable
    public static Uri getUri(@Nullable File file) {
        return file != null ? Uri.fromFile(file) : null;
    }

    @Nullable
    public static File getPathWithoutFilename(@Nullable File file) {
        if (file == null) return null;
        if (file.isDirectory()) return file;
        
        String path = file.getAbsolutePath();
        int lastSlash = path.lastIndexOf(File.separator);
        if (lastSlash == -1) return new File(path);
        
        return new File(path.substring(0, lastSlash));
    }

    @NonNull
    public static String getMimeType(@NonNull File file) {
        String extension = getExtension(file.getName());
        if (extension != null && extension.length() > 1) {
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1).toLowerCase());
            if (mimeType != null) return mimeType;
        }
        return "application/octet-stream";
    }

    @NonNull
    public static String getMimeType(@NonNull Context context, @NonNull Uri uri) {
        String path = getPath(context, uri);
        return path != null ? getMimeType(new File(path)) : "application/octet-stream";
    }

    public static boolean isExternalStorageDocument(@NonNull Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(@NonNull Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(@NonNull Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(@NonNull Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Nullable
    public static String getDataColumn(@NonNull Context context, @NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {column};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting data column: " + uri, e);
        }
        return null;
    }

    @Nullable
    public static String getPath(@NonNull Context context, @NonNull Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @Nullable
    public static File getFile(@NonNull Context context, @Nullable Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    @NonNull
    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Nullable
    public static Bitmap getThumbnail(@NonNull Context context, @NonNull File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    @Nullable
    public static Bitmap getThumbnail(@NonNull Context context, @NonNull Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }

    @Nullable
    public static Bitmap getThumbnail(@NonNull Context context, @Nullable Uri uri, @NonNull String mimeType) {
        if (uri == null) return null;
        if (!isMediaUri(uri)) {
            Log.e(TAG, "Can only retrieve thumbnails for images and videos.");
            return null;
        }

        Bitmap bitmap = null;
        ContentResolver cr = context.getContentResolver();
        try (Cursor cursor = cr.query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int id = cursor.getInt(0);
                if (mimeType.contains("video")) {
                    bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, id, MediaStore.Video.Thumbnails.MINI_KIND, null);
                } else if (mimeType.contains("image")) {
                    bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting thumbnail for " + uri, e);
        }
        return bitmap;
    }

    @NonNull
    public static Intent createGetContentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    @Nullable
    public String getRealPathFromURI(@NonNull Context context, @NonNull Uri contentUri) {
        return getDataColumn(context, contentUri, null, null);
    }
}
