/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jouvieje.FmodEx.Channel;
import org.jouvieje.FmodEx.Defines.FMOD_MODE;
import org.jouvieje.FmodEx.Enumerations.FMOD_CHANNELINDEX;
import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.Misc.BufferUtils;
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

    private boolean paused;

    public OneSound(JDAWEngine jDAWEngine, String path) {
            this.jDAWEngine = jDAWEngine;
            this.sound = new Sound();
            //System.out.println(java.nio.charset.Charset.defaultCharset().name());
            //System.out.println(path.getBytes().length);
            //System.out.println(path.length());

            byte[] bytes = path.getBytes(Charset.forName("UTF-16LE"));
            ByteBuffer buffer = BufferUtils.newByteBuffer(bytes.length + 2);
            buffer.put(bytes);
            BufferUtils.putNullTerminal(buffer);
            BufferUtils.putNullTerminal(buffer);
            buffer.rewind();

            this.result = this.jDAWEngine.getSystem().createStream(buffer, FMOD_MODE.FMOD_UNICODE, null, this.sound);
            SoundUtils.ErrorCheck(result);
        
    }

    public void play() {
        if (this.channel == null) {
            this.channel = new Channel();
            this.result = this.jDAWEngine.getSystem().playSound(FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE, this.sound, false, this.channel);
        } else {
            this.result = this.jDAWEngine.getSystem().playSound(FMOD_CHANNELINDEX.FMOD_CHANNEL_REUSE, this.sound, false, this.channel);
        }
        this.paused = false;
        SoundUtils.ErrorCheck(result);
    }

    public void stop() {
        this.result = this.channel.stop();
        SoundUtils.ErrorCheck(result);
    }

    public void pause() {
        //ByteBuffer buffer = BufferUtils.newByteBuffer(256);
        //this.result = this.channel.isPlaying(buffer);
        //SoundUtils.ErrorCheck(result);
        //int isPlaying = buffer.getInt(0);
        //System.out.println(isPlaying);
        if (paused) {
            this.result = this.channel.setPaused(false);
            paused = !paused;
        }
        else {
            this.result = this.channel.setPaused(true);
            paused = !paused;
        }
        SoundUtils.ErrorCheck(result);
    }

    public double getFrequency() {
        FloatBuffer buffer = BufferUtils.newFloatBuffer(256);
        this.result = this.channel.getFrequency(buffer);
        SoundUtils.ErrorCheck(result);
        double toRet = buffer.get(0);
        return toRet;
    }

}
