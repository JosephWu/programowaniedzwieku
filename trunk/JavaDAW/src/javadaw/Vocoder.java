/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

import java.util.Arrays;

/**
 *
 * @author Stach
 */
public class Vocoder {

    double q = 1;//rezonans (do filtrów)
    //Krańce pasma w którym działa vocoder
    int lowPassFreq = 20;
    int highPassFreq = 10000;
    int passes = 32;//liczba rozpatrywanych pasm
    OneSound oneSound;
    int[] voice;
    int[] sound;

    /*
     * Funkcja zwaraca obwiednię sygnału
     */
    public static int[] getEnvelope(int[] x) {
        int[] envelope = new int[x.length];


        //p i e są indeksami i opisują obsza między maksimami
        int p = 0;//poczatek
        int e = 0;//koniec
        envelope[0] = x[0];
        for (int i = 1; i < x.length - 1; i++) {
            //szukamy maximum
            if (x[i] >= x[i - 1] && x[i] >= x[i + 1] && x[i] > 0) {
                envelope[i] = x[i];
                e = i;
                //uzupełnienie wartości od p do e
                for (int k = 1; k <= e - p; k++) {
                    envelope[p + k] = x[p] + k * (x[e] - x[p]) / (e - p);
                }
                p = e;
            }
        }
        //Teraz możliwe że fragment od ostatniego maksimum do kończa nie jest wypełniony
        //trzeba to naprawić
        for (int k = 1; k < x.length - e; k++) {
            envelope[e + k] = x[e] + k * (x[x.length - 1] - x[e]) / (x.length - e);
        }
        return envelope;
    }



    public int[] bandPassFilter(int[] x, int lowFreq, int highFreq) {
        int[] y = Filters.lowPassFillter(x, highFreq, q);
        y = Filters.highPassFillter(y, lowFreq, q);
        return y;
    }

    public int[] join(int[] sound, int[] envelope) {
        int[] s = new int[sound.length];

        for (int i = 0; i < voice.length; i++) {
            
            s[i] = (int) (sound[i] * envelope[i] / 32767.0);
        }

        return s;
    }

    /*
     * Główna funkcja odpowiada za efekt vocodera
     *
     * Na razie sound (nowy dźwięk nosnej powinien byćtej samej długości co voice)
     */
    public int[] vocoder() {
        Mixer mixer = new Mixer();
        //Teraz filtrujemy pokolej sygnał filtrami pasmo przepustowymi
        for (int i = 0; i < passes; i++) {
            int[] v = bandPassFilter(voice, lowPassFreq + i * (highPassFreq - lowPassFreq) / passes, lowPassFreq + (i + 1) * (highPassFreq - lowPassFreq) / passes);

            //testPlay(v);
            //Obliczamy obwiednię głosu (w danym paśmie)
            int[] envelope = getEnvelope(v);
            for (int k = 0; k < 10; k++) {
                envelope = getEnvelope(envelope);
            }


            //filtrujemy nową nośną
            int[] s = bandPassFilter(sound, lowPassFreq + i * (highPassFreq - lowPassFreq) / passes, lowPassFreq + (i + 1) * (highPassFreq - lowPassFreq) / passes);

            s = join(s, envelope);
            //testPlay(s);

            //testPlay(s);

            if (i == 0) {
                mixer.putSignal(s);
            } else {
                mixer.addSignal(s);
            }
        }

        return mixer.getOutput();
    }

    public void setSound(int[] sound) {
        this.sound = sound;

    }

    public void setVoice(int[] voice) {
        this.voice = voice;
    }

    public int[] bandPassFilterFFT(int[] x, int lowFreq, int highFreq) {
        int blockSize = 512;
        Complex[] s = new Complex[x.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = new Complex(x[i], 0);
        }
        int resolution = 44100 / blockSize;
        for (int i = 0; i < s.length / blockSize; i++) {
            Complex[] tmp = Arrays.copyOfRange(s, i * 512, (i + 1) * 512);
            tmp = new DFT().fft(tmp);
            //teraz wycięcie odpowiedniego pasma
            int l = lowFreq / resolution;
            int r = highFreq / resolution;

            //wycinanie pasma
            for (int k = 0; k < l; k++) {
                tmp[k] = new Complex(0, 0);
                tmp[tmp.length - k - 1] = new Complex(0, 0);
            }
            for (int k = r; k < tmp.length / 2; k++) {
                tmp[k] = new Complex(0, 0);
                tmp[k + blockSize / 2 - 1] = new Complex(0, 0);
            }
            //
            tmp = new DFT().ifft(tmp);

            //przypisanie nowych (przefiltrowanych) wartosci
            int m = 0;
            for (int k = i * blockSize; k < (i + 1) * blockSize; k++) {
                s[k] = tmp[m++];
            }
        }
        //Powrót z Complex do int
        int[] y = new int[x.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = (int) s[i].getRe();
        }

        return y;
    }

    private void testPlay(int[] sound) {
        JavaSound javaSound = new JavaSound();
        javaSound.createSound();
        javaSound.putIntData(sound);
        javaSound.playSound();
    }
}
