
package javadaw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javadaw.recogntion.Case;
import javadaw.recogntion.Knn;
import javadaw.recogntion.LoadTxt;

import javax.swing.JLabel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 */
public class WordRecognizer {

    private int recognizedLabel = 0;
    static Knn knn = new Knn();

    public ArrayList<double[]> spectrogram;
    public int[] values;

    private String fileName = "trening.txt";
    private int treshold = 35000;
    private JLabel jresult;

    //Nagrywanie
    boolean stopCapture = false;
    byte[] streamData;

    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    SourceDataLine sourceDataLine;
    File file=new File("trening.txt");
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


    /**
     *
     * @return
     */
    public ArrayList<Double> start() {
        //Wyliczanie
        int j = 0;
        size = streamData.length;
        double[] fourierValues = new double[size];
        values = new int[size];
        j = 0;
        while (true) {
            if (j == size) {
                break;
            }
            values[j] = (int) (32768 * (streamData[j] / 127.0));
            fourierValues[j] = ((double) (streamData[j]) / 127.0);
            j++;
        }

        int blockSize = 512;
        int overlap = 1;


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

        ArrayList<Double> centroids = spectralCentroid(spectrogram);

        plotSpectrum();
        return centroids;
    }

    /**
     *
     * @param spectogramVertLine
     * @return
     */
    private double findPrazek(double spectogramVertLine[]) {
        double f = 0;
        int step = 1;
        int k = 0;
        for (int i = 0; i < spectogramVertLine.length / 2; i++) {
            double tmp = 0;
            for (int j = 0; j < step; j++) {
                tmp += spectogramVertLine[i + j];
            }
            if (tmp / step > f) {
                f = tmp / step;
                k = i;
            }
        }
        f = k * 8000 / spectogramVertLine.length;
        return f;
    }


    /**
     *
     * @param spectogram1
     * @return
     */
    private ArrayList<Double> spectralCentroid(ArrayList<double[]> spectogram1) {
        ArrayList<Double> f = new ArrayList<Double>();
        double c = 0;
        double a = 0;
        for (double[] ds : spectogram1) {
            double stft[] = ds;
            for (int i = 0; i < stft.length / 2; i++) {
                c += stft[i] * i * 8000.0 / stft.length;
                a += stft[i];
            }
            f.add(c / a);
        }
        return f;
    }


    /**
     *
     * @param label
     * @param result
     */
    public void audioRecorderStart(int label, JLabel result) {
        captureAudio(label);
        jresult = result;
    }


    /**
     *
     * @param label
     */
    public void captureAudio(int label) {
        try {
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            audioInputStream = new AudioInputStream(targetDataLine);
            CaptureThread ct = new CaptureThread(label);
            Thread captureThread = new Thread(ct);
            captureThread.start();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


    /**
     *
     * @return
     */
    private AudioFormat getAudioFormat() {
        float sampleRate = 8000;
        int sampleSizeInBits = 8;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, 1, signed, bigEndian);
    }


    /**
     *
     */
    public void stop() {
        Stop stop = new Stop();
        Thread stoped = new Thread(stop);
        stoped.start();
    }


    /**
     *
     */
    public void plotSpectrum() {
        WaveDataFrame waveDataFrame = new WaveDataFrame(new WaveDataPanel(values, null));
        waveDataFrame.setVisible(true);
        waveDataFrame.validate();
    }


    /**
     *
     * @param features
     * @param label
     */
    private void saveFeautures(ArrayList<Double> features, int label) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < features.size() - 1; i++) {
            sb.append(features.get(i).intValue() + ",");
        }
        sb.append(features.get(features.size() - 1).intValue() + ";" + label + "\r\n");
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.append(sb);
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(WordRecognizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     *
     */
    class CaptureThread extends Thread {
        
        /**
         * 
         */
        int label = 0;

        /**
         *
         * @param label
         */
        public CaptureThread(int label) {
            this.label = label;
        }

        /**
         *
         */
        public CaptureThread() {
            super();
        }

        /**
         *
         */
        @Override
        public void run() {
            byteArrayOutputStream = new ByteArrayOutputStream(size);
            stopCapture = false;
            boolean recording = false;
            int voiceSegments = 0;
            try {
                //Tutaj dostajemy bajty danych
                while (!stopCapture && voiceSegments < 8) {
                    int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    if (cnt > 0) {
                        if (energy(tempBuffer) > treshold) {
                            //Tu warunek na ciszę
                            voiceSegments++;
                            recording = true;
                            byteArrayOutputStream.write(tempBuffer, 0, cnt);
                            continue;
                        }
                        if (recording) {
                            voiceSegments++;
                            byteArrayOutputStream.write(tempBuffer, 0, cnt);
                        }
                    }
                }
                targetDataLine.close();
                if (stopCapture) {
                    targetDataLine.close();
                    return;
                }
                byteArrayOutputStream.close();
                streamData = byteArrayOutputStream.toByteArray();
                streamData = Arrays.copyOf(streamData, streamData.length);

                switch (label) {
                    case 1:
                        saveFeautures(WordRecognizer.this.start(), 1);
                        return;
                    case 2:
                        saveFeautures(WordRecognizer.this.start(), 2);
                        return;
                }

                Case c = new Case();
                ArrayList<Double> f = WordRecognizer.this.start();

                for (int i = 0; i < f.size(); i++) {
                    c.addFeature(f.get(i));
                }

                knn.setTrainCases(new LoadTxt().loadCasesFromTxt(fileName));
                knn.addTestCase(c);
                recognizedLabel = knn.run();
                switch (recognizedLabel) {
                    case 1:
                        jresult.setText("TAK");
                        break;
                    case 2:
                        jresult.setText("NIE");
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }

        /**
         *
         * @param tempBuffer
         * @return
         */
        private double energy(byte[] tempBuffer) {
            double energy = 0;
            for (int i = 0; i < tempBuffer.length; i++) {
                energy += Math.pow(tempBuffer[i], 2);
            }
            return energy;
        }
    }

    /**
     * 
     */
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


}
