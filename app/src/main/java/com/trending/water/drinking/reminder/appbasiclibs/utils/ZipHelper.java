package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {
    private static final String TAG = "ZipHelper";
    private static final int BUFFER_SIZE = 1024;

    public static void unzip(@NonNull String zipFilePath, @NonNull String destinationPath) {
        File destinationDir = new File(destinationPath);
        if (!destinationDir.exists() && !destinationDir.mkdirs()) {
            Log.e(TAG, "Failed to create destination directory: " + destinationPath);
            return;
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String fullPath = destinationPath + File.separator + entry.getName();
                File file = new File(fullPath);
                
                if (entry.isDirectory()) {
                    if (!file.exists() && !file.mkdirs()) {
                        Log.e(TAG, "Failed to create directory: " + fullPath);
                    }
                } else {
                    // Ensure parent directory exists
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists() && !parent.mkdirs()) {
                        Log.e(TAG, "Failed to create parent directory for: " + fullPath);
                    }
                    
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int length;
                        while ((length = zis.read(buffer)) != -1) {
                            fos.write(buffer, 0, length);
                        }
                    }
                    zis.closeEntry();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unzip error", e);
        }
    }

    public void zip(@NonNull String[] filePaths, @NonNull String zipFileName) {
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            for (String path : filePaths) {
                File file = new File(path);
                if (!file.exists()) continue;

                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE)) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int count;
                    while ((count = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        zos.write(buffer, 0, count);
                    }
                    zos.closeEntry();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Zip error", e);
        }
    }

    public void zip(@NonNull List<String> filePaths, @NonNull String zipFileName) {
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            for (String path : filePaths) {
                File file = new File(path);
                if (!file.exists()) continue;

                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE)) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int count;
                    while ((count = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        zos.write(buffer, 0, count);
                    }
                    zos.closeEntry();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Zip error", e);
        }
    }
}
