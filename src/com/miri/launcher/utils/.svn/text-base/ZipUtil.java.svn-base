package com.miri.launcher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip压缩和解压缩工具类
 */
public class ZipUtil {

    /**
     * 压缩目录下的文件
     * @param zipFileName
     * @param inputFile 目录
     * @throws java.lang.Exception
     */
    public static void zip(String zipFileName, String inputFile) throws Exception {
        zip(zipFileName, new File(inputFile));
    }

    /**
     * 压缩文件
     * @param zipFileName
     * @param inputFile 文件
     * @throws java.lang.Exception
     */
    public static void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, "");
        Logger.getLogger().d("压缩成功!");
        out.close();
    }

    private static void zip(ZipOutputStream out, File f, String base) throws Exception {
        Logger.getLogger().d("正在压缩 " + f.getName());
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }

    }

    /**
     * 解压缩
     * @param zipFileName
     * @param outputDirectory 输出目录
     * @throws java.lang.Exception
     */
    public static void unzip(String zipFileName, String outputDirectory) throws Exception {
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
        ZipEntry z;
        while ((z = in.getNextEntry()) != null) {
            Logger.getLogger().d("正在解压 " + z.getName());
            if (z.isDirectory()) {
                String name = z.getName();
                name = name.substring(0, name.length() - 1);
                File f = new File(outputDirectory + File.separator + name);
                f.mkdir();
                Logger.getLogger().d("创建目录 " + outputDirectory + File.separator + name);
            } else {
                File f = new File(outputDirectory + File.separator + z.getName());
                f.createNewFile();
                FileOutputStream out = new FileOutputStream(f);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                out.close();
            }
        }

        in.close();
    }
}
