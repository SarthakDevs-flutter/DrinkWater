package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip_Helper {
    Context mContext;

    public Zip_Helper(Context mContext2) {
        this.mContext = mContext2;
    }

    public static void unzip(String zipFile, String location) throws IOException {
        FileOutputStream fout;
        try {
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            while (true) {
                try {
                    ZipEntry nextEntry = zin.getNextEntry();
                    ZipEntry ze = nextEntry;
                    if (nextEntry != null) {
                        String path = location + File.separator + ze.getName();
                        if (ze.isDirectory()) {
                            File unzipFile = new File(path);
                            if (!unzipFile.isDirectory()) {
                                unzipFile.mkdirs();
                            }
                        } else {
                            fout = new FileOutputStream(path, false);
                            for (int c = zin.read(); c != -1; c = zin.read()) {
                                fout.write(c);
                            }
                            zin.closeEntry();
                            fout.close();
                        }
                    } else {
                        zin.close();
                        return;
                    }
                } catch (Throwable th) {
                    zin.close();
                    throw th;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zip(String[] _files, String zipFileName) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
            byte[] data = new byte[1024];
            for (int i = 0; i < _files.length; i++) {
                BufferedInputStream origin = new BufferedInputStream(new FileInputStream(_files[i]), 1024);
                out.putNextEntry(new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1)));
                while (true) {
                    int read = origin.read(data, 0, 1024);
                    int count = read;
                    if (read == -1) {
                        break;
                    }
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zip(ArrayList<String> _files, String zipFileName) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
            byte[] data = new byte[1024];
            for (int i = 0; i < _files.size(); i++) {
                BufferedInputStream origin = new BufferedInputStream(new FileInputStream(_files.get(i)), 1024);
                out.putNextEntry(new ZipEntry(_files.get(i).substring(_files.get(i).lastIndexOf("/") + 1)));
                while (true) {
                    int read = origin.read(data, 0, 1024);
                    int count = read;
                    if (read == -1) {
                        break;
                    }
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
