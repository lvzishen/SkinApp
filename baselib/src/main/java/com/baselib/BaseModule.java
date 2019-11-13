package com.baselib;

import java.io.File;

public class BaseModule {


    public static interface ExternalConfig {
        /**
         * 列出当前目录下的所有文件
         *
         * @param file
         * @return
         */
        public String[] listFileX(File file);
    }

    private static ExternalConfig sConfig = null;

    public static void init(ExternalConfig config) {
        sConfig = config;
    }

    public static ExternalConfig getExternalConfig() {
        return sConfig;
    }

}