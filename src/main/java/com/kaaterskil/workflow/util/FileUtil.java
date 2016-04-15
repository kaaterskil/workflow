/**
 *
 */
package com.kaaterskil.workflow.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author bcaple
 */
public class FileUtil {

    public static String readFileFromClasspath(String fileName) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is = FileUtil.class.getResourceAsStream(fileName);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();
    }
}
