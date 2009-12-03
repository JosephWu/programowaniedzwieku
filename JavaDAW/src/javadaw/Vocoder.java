/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

import java.util.Arrays;

/**
 * @author Stach
 */
public class Vocoder {

    /**
     * Wartość reoznansu - do filtrów
     */
    private double q = 1.0;
    /**
     * liczba rozpatrywanych pasm
     */
    private int passes = 32;
    /**
     * Wokal do nałożenia efektu wokodera, tablica int -32k do 32k
     */
    int[] voice;
    /**
     * Nośna do wokodera, tablica int -32k do 32k
     */
    int[] sound;

    /**
     * Konstruktor klasy, w nim ustawiamy tablice int -32k do 32k z sygnałem
     * PCM, Pierwszy argument to wokal do zwokodowania a drugi to nośna.
     *
     * @param voice wokal
     * @param sound nośna
     */
    public Vocoder(int[] voice, int[] sound) {
        this.sound = sound;
        this.voice = voice;
    }

    /**
     * Wywołaj to po utworzeniu obiektu klasy w celu pobrania przetworzonego
     * sygnału.
     *
     * @return wokal po wokoderze
     */
    public int[] getVocodedSignal() {
        return this.vocoder();
    }

    /*
     * Funkcja zwaraca obwiednię sygnału
     */
    public static int[] getEnvelope(int[] x) {
        int[] envelope = new int[x.length];
        //start i end są indeksami i opisują obsza między maksimami
        int start = 0;//poczatek
        int end = 0;//koniec
        envelope[0] = x[0];
        for (int i = 1; i < x.length - 1; i++) {
            //szukamy maximum
            if (x[i] >= x[i - 1] && x[i] >= x[i + 1] && x[i] > 0) {
                end = i;
                envelope[end] = x[end];
                double divider = (double) (x[end] - x[start]) / (double) (end - start);
                //uzupełnienie wartości od start do end
                for (int k = 0; k < end - start; k++) {
                    int xPosition = start + k;
                    envelope[xPosition] = (int) (divider * (xPosition - start) + x[start]);
                }
                start = end;
            }
        }
        //Teraz możliwe że fragment od ostatniego maksimum do kończa nie jest wypełniony
        //trzeba to naprawić
        for (int k = 1; k < x.length - end; k++) {
            envelope[end + k] = x[end] + k * (x[x.length - 1] - x[end]) / (x.length - end);
        }
        return envelope;
    }

    /**
     * Funkcja realizuje połączenie filtr pasmoprzepustowy jako
     * połączenie szeregowe filtru dolno i górno przepustowego
     * @param x sygnał
     * @param lowFreq
     * @param highFreq
     * @return
     */
    public int[] bandPassFilter(int[] x, int lowFreq, int highFreq) {
        int[] y = Filters.lowPassFillter(x, highFreq, q);
        y = Filters.highPassFillter(y, lowFreq, q);
        return y;
    }

    /**
     * "Nałożenie" obwiedni głosu na sygnał nośnej
     * @param sound - sygnał nowej nosnej
     * @param envelope - obwiednia
     * @return
     */
    public int[] join(int[] sound, int[] envelope) {
        int[] output = new int[envelope.length];
        int k = 0;
        for (int i = 0; i < voice.length; i++) {
            if (k >= sound.length) {
                k = 0;
            }
            //To chyba nie jest za dobre podejście na połączenie obwiedni z sygnałem
            output[i] = (int) ((sound[k++] * envelope[i]) / 32767.0);
        }
        return output;
    }

    /*
     * Główna funkcja odpowiada za efekt vocodera
     */
    public int[] vocoder() {
        Mixer mixer = new Mixer();
        //Teraz filtrujemy pokolej sygnał filtrami pasmo przepustowymi
        int[] lowFrequTable = {25, 50, 100, 200, 400, 800, 1600, 3200, 6400, 12800};
        int[] highFrequTable = {50, 100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600};
        passes = 8;

        for (int i = 0; i < passes; i++) {
            int lowFrequ = lowFrequTable[i];
            int highFreq = highFrequTable[i];
            int[] v = bandPassFilter(voice, lowFrequ, highFreq);


            //testPlay(v);
            //Obliczamy obwiednię głosu (w danym paśmie)

            //Ta metoda nie jest chyba najlepiej wykonana
            int[] envelope = getEnvelope(v);

            //Obliczenia w pętli mają na celu "wygładzenie" obwiedni
            //(ilość iteracji powinna zostać dobrana eksperymentalnie)

            for (int k = 0; k < 2; k++) {
                envelope = getEnvelope(envelope);
            }

            //Generacja nośnej (w tym wypadku piły)
            int[] s = new SoundGenerator().generateSound(
                    SoundGenerator.SQUARE_SIMPLE_WAVE, v.length,(lowFrequ), 10);

            // envelope powstał z wokalu, więc on jest dla nas wyznacznikiem,
            // a s albo musi byc odpowiednio dlugie, albo zostac powielone
            s = bandPassFilter(s, lowFrequ, highFreq);

            int[] outputVocoded = join(s, envelope);

            //synteza nowych sygnałów
            if (i == 0) {
                mixer.putSignal(outputVocoded);
            } else {
                mixer.addSignal(outputVocoded);
            }
        }
        Ampli ampli = new Ampli();
        return ampli.normalizeSingal(mixer.getOutput());
    }

    /**
     *
     * @param sound
     */
    public void setSound(int[] sound) {
        this.sound = sound;
    }

    /**
     *
     * @param voice
     */
    public void setVoice(int[] voice) {
        this.voice = voice;
    }

    /**
     *Filtr pasmowy oparty o FFT
     *<b>NIE DZIAŁA</b>
     * @param x
     * @param lowFreq
     * @param highFreq
     * @return
     */
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
            int lowFreqThreshold = lowFreq / resolution;
            int highFreqThreshold = highFreq / resolution;

            //wycinanie pasma
            for (int k = 0; k < lowFreqThreshold; k++) {
                tmp[k] = new Complex(0, 0);
                tmp[tmp.length - k - 1] = new Complex(0, 0);
            }
            for (int k = highFreqThreshold; k < tmp.length / 2; k++) {
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

    /**
     * Funkcja pozwalająca na odsłuch analizowanych fragmentów, w celu
     * dokonania obiektywnej oceny działania programu przez urzytkownika.
     *
     * @param sound
     */
    private void testPlay(int[] sound) {
        JavaSound javaSound = new JavaSound();
        javaSound.createSound();
        javaSound.putIntData(sound);
        javaSound.playSound();
    }
}
