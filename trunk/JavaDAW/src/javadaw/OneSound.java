/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import org.jouvieje.FmodEx.Channel;
import org.jouvieje.FmodEx.Defines.FMOD_MODE;
import org.jouvieje.FmodEx.Enumerations.FMOD_CHANNELINDEX;
import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.Misc.BufferUtils;
import org.jouvieje.FmodEx.Sound;

import static org.jouvieje.FmodEx.Defines.FMOD_TIMEUNIT.*;

import java.util.ArrayList;


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

    private boolean streamed;

    private int bytesLength;
    private int samplesLength;
    private int miliSecondsLength;

    private double frequency;

    private String path;

    public OneSound(JDAWEngine jDAWEngine, String path, boolean streamed) {
        this.jDAWEngine = jDAWEngine;
        this.streamed = streamed;
        this.path = path;
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

        if (streamed) {
            this.result = this.jDAWEngine.getSystem().createStream(buffer, FMOD_MODE.FMOD_UNICODE, null, this.sound);
        } else {
            this.result = this.jDAWEngine.getSystem().createSound(buffer, FMOD_MODE.FMOD_UNICODE, null, this.sound);
        }
        SoundUtils.ErrorCheck(result);

        IntBuffer buffer2 = BufferUtils.newIntBuffer(256);
        this.result = this.sound.getLength(buffer2, FMOD_TIMEUNIT_PCMBYTES);
        SoundUtils.ErrorCheck(result);
        this.bytesLength = buffer2.get(0);

        this.result = this.sound.getLength(buffer2, FMOD_TIMEUNIT_PCM);
        SoundUtils.ErrorCheck(result);
        this.samplesLength = buffer2.get(0);

        this.result = this.sound.getLength(buffer2, FMOD_TIMEUNIT_MS);
        SoundUtils.ErrorCheck(result);
        this.miliSecondsLength = buffer2.get(0);

        this.frequency = this.samplesLength * 1000.0 / this.miliSecondsLength;

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

    public int getCroseings(int offset, int length) {
        int toRet = 0;
        ByteBuffer[] bufferPtr1 = new ByteBuffer[1];
        ByteBuffer[] bufferPtr2 = new ByteBuffer[1];
        IntBuffer bufferLen1 = BufferUtils.newIntBuffer(256);
        IntBuffer bufferLen2 = BufferUtils.newIntBuffer(256);

        IntBuffer intBuffer = BufferUtils.newIntBuffer(256);
        this.result = this.sound.getLength(intBuffer, FMOD_TIMEUNIT_PCMBYTES);
        SoundUtils.ErrorCheck(result);

        int size = intBuffer.get(0);
        this.result = this.sound.lock(offset, size, bufferPtr1, bufferPtr2,
                bufferLen1, bufferLen2);
        SoundUtils.ErrorCheck(result);

        byte[] dst = new byte[2];
        int j = 0;
        while (true) {
            if (j == size/2)
                break;
            bufferPtr1[0].get(dst);
            //System.out.println(unsignedByteToInt(dst));
            j++;
        }
        return toRet;
    }


    public void plotSpectogram() {
        ByteBuffer[] bufferPtr1 = new ByteBuffer[1];
        ByteBuffer[] bufferPtr2 = new ByteBuffer[1];
        IntBuffer bufferLen1 = BufferUtils.newIntBuffer(256);
        IntBuffer bufferLen2 = BufferUtils.newIntBuffer(256);

        IntBuffer intBuffer = BufferUtils.newIntBuffer(256);
        this.result = this.sound.getLength(intBuffer, FMOD_TIMEUNIT_PCMBYTES);
        SoundUtils.ErrorCheck(result);

        int size = intBuffer.get(0);
        this.result = this.sound.lock(0, size, bufferPtr1, bufferPtr2,
                bufferLen1, bufferLen2);
        SoundUtils.ErrorCheck(result);

        double[] fourierValues = new double[size/2];
        byte[] dst = new byte[2];
        int j = 0;
        while (true) {
            if (j == size/2)
                break;
            bufferPtr1[0].get(dst);
            fourierValues[j] = (unsignedByteToInt(dst)) / 65535.0;
            j++;
        }

        int blockSize = 512;
        int overlap = 2;
        int m = 256;

        ArrayList<double []> spectrogram = new ArrayList<double []>();
        for (int i = 0; i < size/2; i+= blockSize/overlap) {
            if (i + blockSize > size/2)
                break;
            Complex[] input = new Complex[blockSize];

            for (int k = 0; k < blockSize; k++)
                input[k] = new Complex(fourierValues[i+k]
                        *(0.5 * (1 - Math.cos((2.0*Math.PI*(k-m))/(blockSize-1)))),
                        0.0);
            DFT dft = new DFT();
            Complex[] calculatedDft = dft.fft(input);
            double[] spectogramVertLine = dft.getSpectrum(calculatedDft);
            spectrogram.add(spectogramVertLine);
        }
        SpectrogramPanel spectrogramPanel = new SpectrogramPanel(spectrogram, this);
        SpectrogramFrame spectrogramFrame = new SpectrogramFrame(spectrogramPanel);
        spectrogramFrame.setVisible(true);
        spectrogramFrame.validate();


    }

    public static int unsignedByteToInt(byte[] b) {
        int toRet = 0;
        for (int i = 0; i < b.length; i++) {
            toRet += ((int) (b[i] & 0xFF)) << (i*8);
        }
        return toRet;
    }

    /**
     * @return the bytesLength
     */
    public double getBytesLength() {
        return bytesLength;
    }

    /**
     * @return the samplesLength
     */
    public int getSamplesLength() {
        return samplesLength;
    }

    /**
     * @return the miliSecondsLength
     */
    public int getMiliSecondsLength() {
        return miliSecondsLength;
    }

    /**
     * @return the frequency
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }


}
