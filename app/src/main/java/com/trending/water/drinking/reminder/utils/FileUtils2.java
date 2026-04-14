package com.trending.water.drinking.reminder.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Comparator;

public class FileUtils2 {
    public static final String AUTHORITY = "com.trending.water.drinking.reminder.fileprovider";
    public static final String DOCUMENTS_DIR = "documents";
    public static final String HIDDEN_PREFIX = ".";
    public static final Comparator<File> FILE_COMPARATOR = (f1, f2) ->
            f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
    public static final FileFilter DIRECTORY_FILTER = file ->
            file.isDirectory() && !file.getName().startsWith(HIDDEN_PREFIX);
    public static final FileFilter FILE_FILTER = file ->
            file.isFile() && !file.getName().startsWith(HIDDEN_PREFIX);
    private static final String TAG = "FileUtils2";

    private FileUtils2() {
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

    public static boolean isLocalStorageDocument(@NonNull Uri uri) {
        return AUTHORITY.equals(uri.getAuthority());
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
            Log.e(TAG, "Error getting data column for " + uri, e);
        }
        return null;
    }

    @Nullable
    public static String getPath(@NonNull Context context, @NonNull Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isLocalStorageDocument(uri)) {
                return DocumentsContract.getDocumentId(uri);
            } else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                final String[] contentUriPrefixes = {
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                };

                for (String prefix : contentUriPrefixes) {
                    try {
                        String path = getDataColumn(context, ContentUris.withAppendedId(Uri.parse(prefix), Long.parseLong(id)), null, null);
                        if (path != null) return path;
                    } catch (Exception ignored) {
                    }
                }

                File file = generateFileName(getFileName(context, uri), getDocumentCacheDir(context));
                if (file != null) {
                    saveFileFromUri(context, uri, file.getAbsolutePath());
                    return file.getAbsolutePath();
                }
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
                return getDataColumn(context, contentUri, "_id=?", new String[]{split[1]});
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();
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

    @NonNull
    public static Intent createGetContentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    @NonNull
    public static Intent getViewIntent(@NonNull Context context, @NonNull File file) {
        Uri uri = FileProvider.getUriForFile(context, AUTHORITY, file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = getMimeType(file);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    @NonNull
    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            Log.e(TAG, "Failed to create cache directory: " + dir.getAbsolutePath());
        }
        return dir;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, @NonNull File directory) {
        if (name == null) return null;
        File file = new File(directory, name);
        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }
            int index = 0;
            while (file.exists()) {
                index++;
                file = new File(directory, fileName + "(" + index + ")" + extension);
            }
        }
        try {
            if (file.createNewFile()) return file;
        } catch (IOException e) {
            Log.w(TAG, "Could not create new file: " + file.getAbsolutePath(), e);
        }
        return null;
    }

    public static void saveFileFromUri(@NonNull Context context, @NonNull Uri uri, @NonNull String destinationPath) {
        try (InputStream is = context.getContentResolver().openInputStream(uri);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false))) {
            if (is == null) return;
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error saving file from URI: " + uri, e);
        }
    }

    @Nullable
    public static byte[] readBytesFromFile(@NonNull String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return null;
        byte[] bytesArray = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(bytesArray);
            if (bytesRead != bytesArray.length) {
                Log.w(TAG, "Could not read all bytes from file: " + filePath);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading bytes from file: " + filePath, e);
            return null;
        }
        return bytesArray;
    }

    @Nullable
    public static File createTempImageFile(@NonNull Context context, @NonNull String fileName) throws IOException {
        return File.createTempFile(fileName, ".jpg", getDocumentCacheDir(context));
    }

    @Nullable
    public static String getFileName(@NonNull Context context, @NonNull Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType != null) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
                    if (nameIndex != -1) return cursor.getString(nameIndex);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file name for " + uri, e);
            }
        }
        String path = getPath(context, uri);
        if (path == null) return getName(uri.toString());
        return new File(path).getName();
    }

    @Nullable
    public static String getName(@Nullable String filename) {
        if (filename == null) return null;
        int lastSlash = filename.lastIndexOf('/');
        return filename.substring(lastSlash + 1);
    }
}
