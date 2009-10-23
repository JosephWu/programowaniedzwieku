/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.FmodEx;

/**
 *
 * @author pioras
 */
public class SoundUtils {

    public static void ErrorCheck(FMOD_RESULT result) {
        if(result != FMOD_RESULT.FMOD_OK) {
            System.out.println();
            System.out.println("FMOD error: " + result.asInt() +
                    FmodEx.FMOD_ErrorString(result));
        }
    }

}
