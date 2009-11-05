
package javadaw;

import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.Enumerations.FMOD_SOUND_FORMAT;
import org.jouvieje.FmodEx.Sound;
import org.jouvieje.FmodEx.Structures.FMOD_CREATESOUNDEXINFO;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.*;

/**
 *
 * @author pioras
 */
public class SoundRecorder {
    
    private JDAWEngine jDAWEngine;
    FMOD_CREATESOUNDEXINFO exinfo;
    private Sound sound;

    final int defaultFrequency = 44100;
    final int numberOfChannels = 1;
    final int numberOfBytesPerSample = 2;
    final boolean looping = false;
    
    final int mySoundLength = 1;

    private FMOD_RESULT result;

    public SoundRecorder(JDAWEngine jDAWEngine) {
        this.jDAWEngine = jDAWEngine;
        this.exinfo = new FMOD_CREATESOUNDEXINFO();

        this.exinfo.setDefaultFrequency(defaultFrequency);
        this.exinfo.setFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_PCM16);
        this.exinfo.setNumChannels(numberOfChannels);
        this.exinfo.setLength(defaultFrequency * numberOfBytesPerSample
                * numberOfChannels * mySoundLength);

        this.jDAWEngine.getSystem()
                .createSound((String) null, FMOD_2D | FMOD_SOFTWARE | FMOD_OPENUSER, exinfo, sound);
    }

    public void startRecording() {
        result = this.jDAWEngine.getSystem()
                .recordStart(this.jDAWEngine.recordDeviceInfo.getChoosenDriver(), sound, looping);
        SoundUtils.ErrorCheck(result);
    }

}
