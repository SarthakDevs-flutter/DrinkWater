package com.trending.water.drinking.reminder.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.Comparator;

public class FileUtils {
    public static final String HIDDEN_PREFIX = ".";
    public static final String MIME_TYPE_APP = "application/*";
    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    static final String TAG = "FileUtils";
    private static final boolean DEBUG = false;
    public static Comparator<File> sComparator = new Comparator<File>() {
        public int compare(File f1, File f2) {
            return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
        }
    };
    public static FileFilter sDirFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory() && !file.getName().startsWith(".");
        }
    };
    public static FileFilter sFileFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isFile() && !file.getName().startsWith(".");
        }
    };

    private FileUtils() {
    }

    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }
        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        }
        return "";
    }

    public static boolean isLocal(String url) {
        if (url == null || url.startsWith("http://") || url.startsWith("https://")) {
            return false;
        }
        return true;
    }

    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    public static File getPathWithoutFilename(File file) {
        if (file == null) {
            return null;
        }
        if (file.isDirectory()) {
            return file;
        }
        String filename = file.getName();
        String filepath = file.getAbsolutePath();
        String pathwithoutname = filepath.substring(0, filepath.length() - filename.length());
        if (pathwithoutname.endsWith("/")) {
            pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
        }
        return new File(pathwithoutname);
    }

    public static String getMimeType(File file) {
        String extension = getExtension(file.getName());
        if (extension.length() > 0) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
        }
        return "application/octet-stream";
    }

    public static String getMimeType(Context context, Uri uri) {
        return getMimeType(new File(getPath(context, uri)));
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

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, (String) null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            }
            if (cursor == null) {
                return null;
            }
            cursor.close();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getPath(Context context, Uri uri) {
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, (String) null, (String[]) null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        } else if (isDownloadsDocument(uri)) {
            return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), (String) null, (String[]) null);
        } else if (isMediaDocument(uri)) {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String type = split2[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, contentUri, "_id=?", new String[]{split2[1]});
        }
        return null;
    }

    public static File getFile(Context context, Uri uri) {
        String path;
        if (uri == null || (path = getPath(context, uri)) == null || !isLocal(path)) {
            return null;
        }
        return new File(path);
    }

    public static String getReadableFileSize(int size) {
        DecimalFormat dec = new DecimalFormat("###.#");
        float fileSize = 0.0f;
        String suffix = " KB";
        if (size > 1024) {
            fileSize = (float) (size / 1024);
            if (fileSize > 1024.0f) {
                fileSize /= 1024.0f;
                if (fileSize > 1024.0f) {
                    fileSize /= 1024.0f;
                    suffix = " GB";
                } else {
                    suffix = " MB";
                }
            }
        }
        return String.valueOf(dec.format((double) fileSize) + suffix);
    }

    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    public static Bitmap getThumbnail(Context context, Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004b, code lost:
        if (r9 != null) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004d, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
        if (r9 == null) goto L_0x005c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Bitmap getThumbnail(Context r10, Uri r11, String r12) {
        /*
            boolean r0 = isMediaUri(r11)
            r1 = 0
            if (r0 != 0) goto L_0x000f
            java.lang.String r0 = "FileUtils"
            java.lang.String r2 = "You can only retrieve thumbnails for images and videos."
            android.util.Log.e(r0, r2)
            return r1
        L_0x000f:
            r0 = 0
            if (r11 == 0) goto L_0x005c
            android.content.ContentResolver r8 = r10.getContentResolver()
            r9 = r1
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r2 = r8
            r3 = r11
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            r9 = r2
            boolean r2 = r9.moveToFirst()     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            if (r2 == 0) goto L_0x004b
            r2 = 0
            int r2 = r9.getInt(r2)     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            java.lang.String r3 = "video"
            boolean r3 = r12.contains(r3)     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            r4 = 1
            if (r3 == 0) goto L_0x003d
            long r5 = (long) r2     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            android.graphics.Bitmap r1 = android.provider.MediaStore.Video.Thumbnails.getThumbnail(r8, r5, r4, r1)     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            r0 = r1
            goto L_0x004b
        L_0x003d:
            java.lang.String r3 = "image/*"
            boolean r3 = r12.contains(r3)     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            if (r3 == 0) goto L_0x004b
            long r5 = (long) r2     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            android.graphics.Bitmap r1 = android.provider.MediaStore.Images.Thumbnails.getThumbnail(r8, r5, r4, r1)     // Catch:{ Exception -> 0x0058, all -> 0x0051 }
            r0 = r1
        L_0x004b:
            if (r9 == 0) goto L_0x005c
        L_0x004d:
            r9.close()
            goto L_0x005c
        L_0x0051:
            r1 = move-exception
            if (r9 == 0) goto L_0x0057
            r9.close()
        L_0x0057:
            throw r1
        L_0x0058:
            r1 = move-exception
            if (r9 == 0) goto L_0x005c
            goto L_0x004d
        L_0x005c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trending.water.drinking.reminder.utils.FileUtils.getThumbnail(android.content.Context, android.net.Uri, java.lang.String):android.graphics.Bitmap");
    }

    public static Intent createGetContentIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("*/*");
        intent.addCategory("android.intent.category.OPENABLE");
        return intent;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
