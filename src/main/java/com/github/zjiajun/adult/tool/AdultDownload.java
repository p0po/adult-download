package com.github.zjiajun.adult.tool;

import com.github.zjiajun.adult.exception.AdultException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import static com.github.zjiajun.adult.tool.AdultTool.*;

/**
 * Created by zhujiajun
 * 16/6/27 14:38
 *
 * 下载工具类
 */
public final class AdultDownload {

    public static void down2File(AdultDownInfo downInfo) {
        down2File(downInfo,null);
    }

    public static void down2File(AdultDownInfo downInfo, DownloadCallback callback) {
        Objects.requireNonNull(downInfo);
        FileOutputStream fileOutputStream = null;
        try {
            File path = new File(downInfo.getFilePath());
            if (!path.exists()) path.mkdirs();
            HttpURLConnection connection = (HttpURLConnection) new URL(downInfo.getUrl()).openConnection();
            connection.addRequestProperty("User-Agent", randomUa());
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            fileOutputStream = new FileOutputStream(downInfo.getFilePathName());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            if (callback != null) callback.call();
        } catch (IOException e) {
            throw new AdultException(LoggerTool.getTrace(e));
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface DownloadCallback {
        void call();
    }

}
