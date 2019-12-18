package com.datasource.as.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static String readFile(String path, Charset encoding) {
        try {
            encoding = encoding == null ? StandardCharsets.UTF_8 : encoding;
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readFile(String path) {
        return FileUtil.readFile(path, StandardCharsets.UTF_8);
    }

}
