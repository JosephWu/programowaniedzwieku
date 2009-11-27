
package javadaw;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author pioras
 */
public class JavaSound {

    private AudioFormat audioFormat;
    private SourceDataLine line;

    public int frequency = 44100;
    public int sampleSizeBits = 16;
    public int numberOfChannels = 1;
    public boolean bigEndian = true;
    public boolean isSigned = true;

    private byte[] soundData;

    public JavaSound() {

    }


    public void createSound() {
        try {
            audioFormat = new AudioFormat(frequency, sampleSizeBits, numberOfChannels, isSigned, bigEndian);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(JavaSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void playSound() {
        try {
            line.open();
            line.start();
            {
                line.write(soundData, 0, soundData.length);
            }
        } catch (LineUnavailableException ex) {
            Logger.getLogger(JavaSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopSound() {
        line.stop();
        line.drain();
        line.close();
    }

    private void intToBytes(int[] values) {
        this.soundData = new byte[values.length * (this.sampleSizeBits / 8)];
        for (int i = 0; i < values.length; i++) {
            soundData[2*i] = (byte) (((values[i] >> 8) & 0x000000FF));
            soundData[2*i + 1] = (byte) (values[i] & 0x000000FF);
        }
    }

    public void putIntData(int[] values) {
        this.intToBytes(values);
    }

}
