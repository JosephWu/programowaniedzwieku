/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

import java.io.File;

/**
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class FilterUtils {

    public final static String wav = "wav";
    public final static String mp3 = "mp3";
    public final static String ogg = "ogg";
    public final static String wma = "wma";
    public final static String asf = "asf";
    public final static String flac = "flac";
    public final static String aiff = "aiff";

    /**
     * Metoda pobiera rozszerzenie pliku.
     *
     * @param f plik
     * @return ext rozszerzenie pliku
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
