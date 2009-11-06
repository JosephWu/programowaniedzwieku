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

    public WordRecognizer(OneSound oneSound) {
        this.sound = oneSound.getSound();
        this.bytesLength = (int) oneSound.getBytesLength();

    }

    public ArrayList<Double> start() {
        ByteBuffer[] bufferPtr1 = new ByteBuffer[1];
        ByteBuffer[] bufferPtr2 = new ByteBuffer[1];
        IntBuffer bufferLen1 = BufferUtils.newIntBuffer(256);
        IntBuffer bufferLen2 = BufferUtils.newIntBuffer(256);

        IntBuffer intBuffer = BufferUtils.newIntBuffer(256);


        int size = bytesLength;
        this.result = this.sound.lock(0, size, bufferPtr1, bufferPtr2,
                bufferLen1, bufferLen2);
        SoundUtils.ErrorCheck(result);


        //Wyliczanie wartości połaczone z detekcją ciszy
        byte[] dst = new byte[2];
        int j = 0;
        while (true) {
            if (j == size / 4) {
                break;
            }
            bufferPtr1[0].get(dst);
            double f = ((OneSound.unsignedByteToInt(dst)) / 32767.0);
            bufferPtr1[0].get(dst);//Drugi kanał bo stereo :(
            //System.out.println("--------------- "+f);

            if (Math.abs(f) > silienceTreshold) {
                break;
            }
            j++;
        }

        double[] fourierValues = new double[bytesLength / 4 - j];
        values = new int[bytesLength / 4 - j];
        j = 0;
        while (true) {
            if (j == fourierValues.length - 2) {
                break;
            }
            bufferPtr1[0].get(dst);
            values[j] = ((OneSound.unsignedByteToInt(dst)));
            fourierValues[j] = ((OneSound.unsignedByteToInt(dst)) / 32767.0);
            bufferPtr1[0].get(dst);//Drugi kanał bo stereo :(
            // System.out.println(fourierValues[j]);
            j++;
        }
        //NA tym etapie tablica ma "wyciętą cisze z początku nagrania"
        //wycinamy ciszę z końca


        for (j = fourierValues.length - 1; j >= 0; j--) {
            if (fourierValues[j] > silienceTreshold) {
                break;
            }
        }

        fourierValues = Arrays.copyOf(fourierValues, j + 1);
        values = Arrays.copyOf(values, j + 1);




        int blockSize = (int) Math.pow(2, Math.floor(log2(fourierValues.length / 16)));
        int overlap = 2;


        spectrogram = new ArrayList<double[]>();

        //For tests
        StringBuilder answer = new StringBuilder();
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

        int a = 0;
        int e = 0;
        for (int i = 0; i < frequency.size(); i++) {
            if (frequency.get(i) > 19000) {
                //System.out.println("Wywalam "+frequency.get(i));
                frequency.remove(i);
                i = 0;
            }
        }


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
        for (int i = 0; i < spectogramVertLine.length - step; i++) {
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
        f = k * 22100 / spectogramVertLine.length;
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
            c+=stft[i]*i*22050/stft.length;
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
}
