/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import org.jouvieje.FmodEx.Channel;
import org.jouvieje.FmodEx.Enumerations.FMOD_CHANNELINDEX;
import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.Sound;

import static org.jouvieje.FmodEx.Defines.FMOD_MODE.FMOD_DEFAULT;
import static org.jouvieje.FmodEx.Enumerations.FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE;

/**
 *
 * @author pioras
 */
public class OneSound {
    
    private JDAWEngine jDAWEngine;
    private Sound sound;
    private FMOD_RESULT result;
    private Channel channel;

    public OneSound(JDAWEngine jDAWEngine, String path) {
        this.jDAWEngine = jDAWEngine;
        this.channel = new Channel();
        this.sound = new Sound();
        this.result = this.jDAWEngine.getSystem().createSound(path, FMOD_DEFAULT, null, this.sound);
        SoundUtils.ErrorCheck(result);
    }

    public void play() {
        this.result = this.jDAWEngine.getSystem().playSound(FMOD_CHANNEL_FREE, this.sound, false, this.channel);
        SoundUtils.ErrorCheck(result);
    }

}
