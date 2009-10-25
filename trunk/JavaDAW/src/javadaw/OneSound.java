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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jouvieje.FmodEx.Channel;
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
    private String path;

    private boolean paused;

    public OneSound(JDAWEngine jDAWEngine, String path) {
        this.jDAWEngine = jDAWEngine;
        this.sound = new Sound();
        this.result = this.jDAWEngine.getSystem().createStream(path, FMOD_DEFAULT, null, this.sound);
        this.path = path;
        /**if (result == FMOD_RESULT.FMOD_ERR_FILE_NOTFOUND) {
            File file = new File(path);
            if (file.exists()) {
                String tmpPath = path.replace('ę', 'e').replace('ó', 'o').replace('ą', 'a')
                        .replace('ś', 's').replace('ł', 'l').replace('ż', 'z')
                        .replace('ź', 'z').replace('ć', 'c').replace('ń', 'n');
                int lastIndexOfDir = tmpPath.lastIndexOf("/");
                File file2 = new File(tmpPath);
                
                FileInputStream from = null;
                FileOutputStream to = null;
                try {
                    from = new FileInputStream(file);
                    to = new FileOutputStream(file2);
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = from.read(buffer)) != -1) {
                        to.write(buffer, 0, bytesRead); // write
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(OneSound.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OneSound.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (from != null) {
                        try {
                            from.close();
                        } catch (IOException e) {
                        }
                    }
                    if (to != null) {
                        try {
                            to.close();
                        } catch (IOException e) {
                        }
                    }
                
                this.result = this.jDAWEngine.getSystem().createStream(file2.getAbsolutePath(), FMOD_DEFAULT, null, this.sound);
            }
        }}**/
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

}
