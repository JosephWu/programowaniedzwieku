
package javadaw;

import java.nio.ByteBuffer;
import javax.swing.JOptionPane;
import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.Enumerations.FMOD_SOUND_FORMAT;
import org.jouvieje.FmodEx.Misc.BufferUtils;
import org.jouvieje.FmodEx.Sound;
import org.jouvieje.FmodEx.Structures.FMOD_CREATESOUNDEXINFO;
import static org.jouvieje.FmodEx.Defines.FMOD_INITFLAGS.FMOD_INIT_NORMAL;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_2D;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_LOOP_NORMAL;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_LOOP_OFF;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_OPENUSER;
import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_SOFTWARE;
import static org.jouvieje.FmodEx.Misc.BufferUtils.SIZEOF_SHORT;

/**
 *
 * @author pioras
 */
public class SoundRecorder {
    
    private JDAWEngine jDAWEngine;
    private Sound sound;

    private final int defaultFrequency = 44100;
    private final int numberOfChannels = 1;
    private final int numberOfBytesPerSample = 2;
    private final boolean looping = false;
    
    private int mySoundLength = 2;

    private FMOD_RESULT result;

    public SoundRecorder(JDAWEngine jDAWEngine) {
        this.jDAWEngine = jDAWEngine;
        this.sound = new Sound();
    }

    public void init() {
        FMOD_CREATESOUNDEXINFO exinfo = FMOD_CREATESOUNDEXINFO.create();
        exinfo.setNumChannels(numberOfChannels);
        exinfo.setFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_PCM16);
        exinfo.setDefaultFrequency(defaultFrequency);
        exinfo.setLength(exinfo.getDefaultFrequency() * SIZEOF_SHORT * exinfo.getNumChannels() * mySoundLength);

        this.result = this.jDAWEngine.getSystem().createSound(
                (String) null, FMOD_2D | FMOD_SOFTWARE | FMOD_OPENUSER, exinfo, sound);
        SoundUtils.ErrorCheck(result);
    }

    public void startRecording() {
        result = this.jDAWEngine.getSystem()
                .recordStart(this.jDAWEngine.recordDeviceInfo.getChoosenDriver(), sound, looping);
        SoundUtils.ErrorCheck(result);
        boolean recording = true;
        ByteBuffer buffer = BufferUtils.newByteBuffer(256);
        JOptionPane pane = new JOptionPane();
        pane.setMessage("Trwa nagrywanie...");
        pane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        pane.setVisible(true);
        while (recording) {
            result = this.jDAWEngine.getSystem()
                    .isRecording(this.jDAWEngine.recordDeviceInfo.getChoosenDriver(), buffer);
            SoundUtils.ErrorCheck(result);
            recording = buffer.get(0) != 0;
            this.jDAWEngine.getSystem().update();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pane.setVisible(false);
    }

}
