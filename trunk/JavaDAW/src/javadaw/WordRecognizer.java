/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import javadaw.recogntion.Case;
import javadaw.recogntion.Knn;
import org.jouvieje.FmodEx.Enumerations.FMOD_RESULT;
import org.jouvieje.FmodEx.Misc.BufferUtils;
import org.jouvieje.FmodEx.Sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


/**
 *
 * @author Stach
 */
public class WordRecognizer {

    static Knn knn = new Knn();
    private int featuresNumber = 20;
    private Sound sound;
    private int bytesLength;
    public ArrayList<double[]> spectrogram;
    public int[] values;
    private double silienceTreshold = 0.1;
    private FMOD_RESULT result;

    private int treshold=140;
    //Nagrywanie
        boolean stopCapture = false;
    byte[] streamData;
    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    File file;
    String outputFileName;
    static int time = 2000;
    static int seconds = 2;
    static int channels = 1;
    /** bytes - 1 means 8 bit, 2 means 16 bit, 3 means 24 bit. */
    static int bytes = 1;
    /** Samples per second, sample rate in Hertz. */
    static int samplerate = 8000;
    static int size = samplerate * channels * bytes * 60 * seconds;
    static int timeperiod = 20;
    //static byte tempBuffer[] = new byte[samplerate * channels * bytes / timeperiod];
    static byte tempBuffer[] = new byte[512];
    //

    public WordRecognizer(OneSound oneSound) {
        this.sound = oneSound.getSound();
        this.bytesLength = (int) oneSound.getBytesLength();

    }

    public ArrayList<Double> start() {
//        ByteBuffer[] bufferPtr1 = new ByteBuffer[1];
//        ByteBuffer[] bufferPtr2 = new ByteBuffer[1];
//        IntBuffer bufferLen1 = BufferUtils.newIntBuffer(256);
//        IntBuffer bufferLen2 = BufferUtils.newIntBuffer(256);
//
//        IntBuffer intBuffer = BufferUtils.newIntBuffer(256);


        
//        this.result = this.sound.lock(0, size, bufferPtr1, bufferPtr2,
//                bufferLen1, bufferLen2);
//        SoundUtils.ErrorCheck(result);


        //Wyliczanie
        int j = 0;
        size=streamData.length;
        double[] fourierValues = new double[size];
        values = new int[size];
        j = 0;
        while (true) {
            if (j == size) {
                break;
            }
            values[j] = (streamData[j]);
            fourierValues[j] = ((double)(streamData[j]) / 127.0);
            j++;
        }


        int blockSize = 512;
        int overlap = 2;


        spectrogram = new ArrayList<double[]>();

        ArrayList<Double> frequency = new ArrayList<Double>();
        for (int i = 0; i < fourierValues.length; i += blockSize / overlap) {
            if (i + blockSize > fourierValues.length) {
                break;
            }
            Complex[] input = new Complex[blockSize];

            for (int k = 0; k < blockSize; k++) {
                input[k] = new Complex(fourierValues[i + k] * (0.5 * (1 - Math.cos((2.0 * Math.PI * (k - i)) / (blockSize - 1)))),
                        0.0);
            }
            DFT dft = new DFT();
            Complex[] calculatedDft = dft.fft(input);
            double[] spectogramVertLine = dft.getSpectrum(calculatedDft);
            spectrogram.add(spectogramVertLine);

            frequency.add(findPrazek(spectogramVertLine));
        }

//        int a = 0;
//        int e = 0;
//        for (int i = 0; i < frequency.size(); i++) {
//            if (frequency.get(i) > 19000) {
//                //System.out.println("Wywalam "+frequency.get(i));
//                frequency.remove(i);
//                i = 0;
//            }
//        }

        for (Double double1 : frequency) {
            System.out.println(double1);
        }
        System.out.println("adldasffjdlanald");
        return frequency;

    }

    private double log2(double value) {
        return Math.log(value) / Math.log(2);
    }

    private double findPrazek(double spectogramVertLine[]) {
        double f = 0;
        int step = 1;
        int k = 0;
        //System.out.println("--------------------------------------------");
        for (int i = 0; i < spectogramVertLine.length/2; i++) {
            double tmp = 0;
            //System.out.println(spectogramVertLine[i]);
            for (int j = 0; j < step; j++) {
                tmp += spectogramVertLine[i + j];
            }
            if (tmp / step > f) {
                f = tmp / step;
                k = i;
            }
        }
        f = k * 8000 / spectogramVertLine.length;
        //System.out.println(k);

        return f;
    }

    private ArrayList<Double> spectralCentroid(ArrayList<double[]> spectogram1) {


        ArrayList<Double> f=new ArrayList<Double>();
        double c= 0;
        double a=0;
        for (double[] ds : spectogram1) {
                  double stft[]=ds;
        for(int i=0;i<stft.length/2;i++){
            c+=stft[i]*i*4000/stft.length;
            a+=stft[i];
        }
        f.add(c/a);  
        }
        return f;


    }

    void addPattern(int label) {
        Case case1 = new Case();
        case1.setLabel(label);

        ArrayList<Double> f=new ArrayList<Double>();
        try{
        f = spectralCentroid(spectrogram);
        }catch(Exception e){
            System.out.println("Mów głośniej");
        }
        
        if (f.size() < featuresNumber) {
            int size = f.size();
            for (int i = 0; i < featuresNumber - size; i++) {
                f.add(0.0);
            }
        }
        for (int i = 0; i < featuresNumber; i++) {
            case1.addFeature(f.get(i));
            System.out.println(f.get(i));
        }

        System.out.println("Label: " + label);
        knn.addTrainCase(case1);
    }

    public int test() {
        Case case1 = new Case();
        ArrayList<Double> f;
        try {
            f = start();
             
        } catch (Exception e) {
            System.out.println("Mów głoścniej!!!");
            return 0;
        }
       
        if (f.size() < featuresNumber) {
            int size = f.size();
            for (int i = 0; i < featuresNumber - size; i++) {
                f.add(0.0);
            }
        }
        for (int i = 0; i < featuresNumber; i++) {
            case1.addFeature(f.get(i));
        }
        knn.addTestCase(case1);
        return knn.run();
    }

    public int testWave() {
        ArrayList<Double> freq = new ArrayList<Double>();
        try {
            freq = start();
        } catch (Exception e) {
            System.out.println("Prosze mówić głośniej");
            return 0;
        }
        int a = 0;
        int e = 0;
        for (Double double1 : freq) {
            if (Math.abs(double1 - 700) < 100) {
                a++;
                continue;
            }
            if (Math.abs(double1 - 900) < 100) {
                e++;
                continue;
            }
        }
        if (a < 2 || e < 2) {
            return 0;
        }
        if (a > e) {
            return 1;
        }
        return 2;
    }
    //
        public static void main(String[] args) {
        /*Recorder grabador =  new Recorder("foo1.wav",4,1,16000,3000);
        grabador.AudioRecorder();*/
    }

    public void AudioRecorder() {
        captureAudio();

    }

    public void captureAudio() {
        try {
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            audioInputStream = new AudioInputStream(targetDataLine);
            CaptureThread ct = new CaptureThread();
            Thread captureThread = new Thread(ct);
            captureThread.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }



    private AudioFormat getAudioFormat() {
        float sampleRate = 8000;
        int sampleSizeInBits = 8;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, 1, signed, bigEndian);
    }

    class CaptureThread extends Thread {

        @Override
        public void run() {
            byteArrayOutputStream = new ByteArrayOutputStream(size);
            stopCapture = false;
            boolean recording = false;

            int silenceSegment = 0;
            try {
                //Tutaj dostajemy bajty danych

                while (!stopCapture) {
                    int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    if (cnt > 0) {
                        //System.out.println(sr.energy(tempBuffer));
                        if (energy(tempBuffer)>treshold) {//Tu warunek na ciszę
                            silenceSegment = 0;
                            recording = true;
                            System.out.println("Wykryto mowę");
                            byteArrayOutputStream.write(tempBuffer, 0, cnt);

                        } else {
                            if (recording) {
                                silenceSegment++;
                                //System.out.println(silenceSegment);
                                if (silenceSegment == 4) {
                                    stopCapture = true;
                                    System.out.println("Koniec nagrania");
                                    //this will stop the recording
                                    targetDataLine.close();

                                }
                                byteArrayOutputStream.write(tempBuffer, 0, cnt);
                            }
                        }
                    }
                }
                byteArrayOutputStream.close();
                streamData=byteArrayOutputStream.toByteArray();
                System.out.println(streamData.length);
                WordRecognizer.this.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        private double energy(byte[] tempBuffer) {
            double energy=0;
            for(int i=0;i<tempBuffer.length;i++){
    
            //System.out.println(tempBuffer[i]);
            energy+=Math.pow(tempBuffer[i],2);
            }
            //System.out.println(energy);
            return energy;
        }
    }

    private class SaveThread extends Thread {

        @Override
        public void run() {
            try {
                if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioInputStream)) {
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void stop() {
        Stop stop = new Stop();
        Thread stoped = new Thread(stop);
        stoped.start();
    }

    private class Stop extends Thread {

        @Override
        public void run() {
            try {
                stopCapture = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    private void silenceDetection() {
    }
}
