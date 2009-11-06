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

    private ArrayList<Integer> indexes;

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

    public int getCroseings(int offset, int length, int block) {
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
        bufferPtr1[0].get(dst);
        boolean below = false;
        if (unsignedByteToInt(dst) < block)
            below = true;
        while (true) {
            if (j == size/2-1)
                break;
            bufferPtr1[0].get(dst);
            if (unsignedByteToInt(dst) > block && below == true) {
                below = false;
                toRet++;
            } else if (unsignedByteToInt(dst) < block && below == false) {
                below = true;
                toRet++;
            }
            //System.out.println(unsignedByteToInt(dst));
            j++;
        }
        return toRet;
    }

    public int[] getCroseingsPlus(int offset, int length, int block) {
        
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
        int[] tmpSound = new int[size/2];
        int[] toRet = new int[size/2];

        int j = 0;
        while (true) {
            if (j == size / 2) {
                break;
            }
            bufferPtr1[0].get(dst);
            tmpSound[j] = unsignedByteToInt(dst);
            j++;
        }


        int n = 0;
        int wndSize = 44100 / 8;
        int outResult = 0; int prevOutResult = 0;
        ArrayList<Integer> cutTimeList = new ArrayList<Integer>();
        ArrayList<Integer> frequencyList = new ArrayList<Integer>();
        ArrayList<Integer> tmpFrequencyList = new ArrayList<Integer>();
        while (n+wndSize < tmpSound.length) {
            int k = 0;
            boolean below = false;
            int founded = 0;
            if ((tmpSound[k + n]) < block) {
                below = true;
            }
            k++;
            while (k < wndSize) {
                if (tmpSound[k + n] > block && below == true) {
                    below = false;
                    founded++;
                } else if (tmpSound[k + n] < block && below == false) {
                    below = true;
                    founded++;
                }
                k++;
            }
            outResult = founded * 8;
            if (n == 0) {
                prevOutResult = outResult;
                cutTimeList.add(0);
                frequencyList.add(prevOutResult);
                tmpFrequencyList.add(prevOutResult);
            }
            else
                if (!(prevOutResult-100 < outResult && prevOutResult+100 > outResult)) {
                    cutTimeList.add(n);
                    int buff = 0;
                    for (int i = 0; i < tmpFrequencyList.size(); i++)
                        buff += tmpFrequencyList.get(i);
                    frequencyList.add(buff/tmpFrequencyList.size());
                    tmpFrequencyList.clear();
                }
            prevOutResult = outResult;
            tmpFrequencyList.add(prevOutResult);
            n = n + 44100 / 8 / 2;
        }
        frequencyList.add(prevOutResult);
        cutTimeList.add(n);
        cutTimeList.add(0);
        
        int pos = 0;
        for (int i = 0; i < toRet.length; i++) {
            if (cutTimeList.get(pos) == i)
                pos++;
            toRet[i] = frequencyList.get(pos-1);
        }
        
        return toRet;
    }

    /**public int[] getCroseingsPlus(int offset, int length, int block) {
        int toRet[];
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
        bufferPtr1[0].get(dst);
        boolean below = false;
        indexes = new ArrayList<Integer>();
        if (unsignedByteToInt(dst) < block)
            below = true;
        int changes = 0; int distance = 0; int actualDistance = 0;
        int minimumDistance = 0;
        while (true) {
            if (j == size/2-1)
                break;
            bufferPtr1[0].get(dst);
            if (unsignedByteToInt(dst) > block && below == true) {
                if (!(distance - 20 < actualDistance && actualDistance < distance + 20)) {
                    minimumDistance++;
                    if (minimumDistance > 100) {
                        changes++;
                        indexes.add(j);
                        minimumDistance = 0;
                    }
                }
                distance = actualDistance;
                actualDistance = 0;
                below = false;
                actualDistance++;
            } else if (unsignedByteToInt(dst) < block && below == false) {
                below = true;
                actualDistance++;
            } else {
                actualDistance++;
            }
            j++;
        }
        toRet = new int[changes+1];


        j = 0;
        int endJ = indexes.get(0);
        bufferPtr1[0].rewind();
        bufferPtr1[0].get(dst);
        int nextSoundGen = 0;
        if (unsignedByteToInt(dst) < block)
            below = true;
        while (true) {
            if (j == size/2-1)
                break;
            bufferPtr1[0].get(dst);
            if (unsignedByteToInt(dst) > block && below == true) {
                below = false;
                toRet[nextSoundGen]++;
            } else if (unsignedByteToInt(dst) < block && below == false) {
                below = true;
                toRet[nextSoundGen]++;
            }
            if (j == endJ) {
                nextSoundGen++;
                if(indexes.size() > nextSoundGen) {
                    endJ = indexes.get(nextSoundGen);
                }
            }
            //System.out.println(unsignedByteToInt(dst));
            j++;
        }

        
        return toRet;
    }**/





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
            fourierValues[j] = ((unsignedByteToInt(dst)) + 32767.0) / 65535.0;
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
                        *(0.5 * (1 - Math.cos((2.0*Math.PI*(k-i))/(blockSize-1)))),
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

    public void plotSpectrum() {
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

        int[] values = new int[size/2];
        byte[] dst = new byte[2];
        int j = 0;
        while (true) {
            if (j == size/2)
                break;
            bufferPtr1[0].get(dst);
            values[j] = (unsignedByteToInt(dst));
            j++;
        }
        WaveDataPanel wavDataPanel = new WaveDataPanel(values, this);
        WaveDataFrame waveDataFrame = new WaveDataFrame(wavDataPanel);
        waveDataFrame.setVisible(true);
        waveDataFrame.validate();
    }

    public void generateSound(int sampleRate) {
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

        for (int i = 0; i < size/2; i++) {
            bufferPtr1[0].put(intToByte((int)(32767.0*Math.sin(Math.PI*2.0*(double)i*(double)sampleRate/44100.0))));
        }
        bufferPtr1[0].rewind();
        this.result = this.sound.unlock(bufferPtr1[0], null,
                size, 0);
        SoundUtils.ErrorCheck(result);

    }


    public void generateSoundPlus(int sampleRates[]) {
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

        for (int i = 0; i < size/2; i++) {
            bufferPtr1[0].put(intToByte((int)(32767.0*Math.sin(Math.PI*2.0*(double)i*(double)sampleRates[i]/2/44100.0))));
        }
        bufferPtr1[0].rewind();
        this.result = this.sound.unlock(bufferPtr1[0], null,
                size, 0);
        SoundUtils.ErrorCheck(result);

    }

    public static byte[] intToByte(int value) {
        byte[] toRet = new byte[2];
        toRet[1] = (byte) ((value << 16) >> 24);
        toRet[0] = (byte) ((value << 24) >> 24);
        return toRet;
    }

    public static int unsignedByteToInt(byte[] b) {
        int toRet = 0;
        for (int i = 0; i < b.length; i++) {
            toRet += ((int) (b[i])) << (i*8);
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

    public Sound getSound() {
        return sound;
    }

    public void setBytesLength(int bytesLength) {
        this.bytesLength = bytesLength;
    }

    public void setSamplesLength(int samplesLength) {
        this.samplesLength = samplesLength;
    }


}
