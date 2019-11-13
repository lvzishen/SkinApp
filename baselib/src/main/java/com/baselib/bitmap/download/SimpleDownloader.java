package com.baselib.bitmap.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baselib.utils.ModuleConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

@SuppressLint("DefaultLocale")
public class SimpleDownloader implements Downloader {

    public static boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = "SimpleDownloader";
    private static final int IO_BUFFER_SIZE = 8 * 1024;// 8KB
    private static final long LIMIT_SIZE = 10 * 1024 * 1024;// 10 MB

    private long mLimitSize = LIMIT_SIZE;

    @Override
    public byte[] download(Context context, String urlString) {
        if (urlString == null)
            return null;

        if (urlString.trim().toLowerCase(Locale.US).startsWith("http")) {
            return getFromHttp(urlString);
        } else if (urlString.trim().toLowerCase(Locale.US).startsWith("file:")) {
            try {
                File f = new File(new URI(urlString));
                if (f.exists() && f.canRead()) {
                    return getFromFile(f);
                }
            } catch (URISyntaxException e) {
            }
        } else {
            File f = new File(urlString);
            if (f.exists() && f.canRead()) {
                return getFromFile(f);
            }
        }
        return null;
    }

    private byte[] getFromFile(File file) {
        if (file == null)
            return null;
        if (mLimitSize > 0 && file.length() > mLimitSize) {
            return null;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
                    // DO NOTHING
                }
            }
        }
        return null;
    }

    private byte[] getFromHttp(String urlString) {
        HttpURLConnection conn = null;
        BufferedOutputStream out = null;
        FlushedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            in = new FlushedInputStream(new BufferedInputStream(
                    conn.getInputStream(), IO_BUFFER_SIZE));
            if (mLimitSize > 0) {
                int contentLength = conn.getContentLength();
                if (DEBUG) {
                    Log.d(TAG, "getFromHttp-->contentLength:" + contentLength + " mLimitSize:" + mLimitSize);
                }
                if (contentLength > mLimitSize) {
                    return null;
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b;
            while ((b = in.read()) != -1) {
                baos.write(b);
            }
            return baos.toByteArray();
        } catch (IOException e) {
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return null;
    }

    public void setLimitSize(long limitSize) {
        this.mLimitSize = limitSize;
    }

    public class FlushedInputStream extends FilterInputStream {

        protected FlushedInputStream(InputStream in) {
            super(in);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int by_te = read();
                    if (by_te < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

}
