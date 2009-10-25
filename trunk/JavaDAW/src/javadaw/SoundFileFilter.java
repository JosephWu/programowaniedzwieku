/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author pioras
 */
public class SoundFileFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
            return true;
        }

        String extension = FilterUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(FilterUtils.aiff) ||
                    extension.equals(FilterUtils.asf) ||
                    extension.equals(FilterUtils.flac) ||
                    extension.equals(FilterUtils.mp3) ||
                    extension.equals(FilterUtils.ogg) ||
                    extension.equals(FilterUtils.wav) ||
                    extension.equals(FilterUtils.wma)){
                    return true;
            } else {
                return false;
            }
        }

        return false;
	}

	public String getDescription() {
		return "Pliki muzyczne (WAV, MP3, OGG, WMA, ASF, FLAC lub AIFF)";
	}
}
