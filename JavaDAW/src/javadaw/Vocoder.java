/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

/**
 *
 * @author Stach
 */
public class Vocoder {

    double q = 1;//rezonans (do filtrów)
    //Krańce pasma w którym działa vocoder
    int lowPassFreq = 500;
    int highPassFreq = 3500;
    int passes = 10;//liczba rozpatrywanych pasm
    OneSound oneSound;
    int[] voice;
    int[] sound;

    /*
     * Funkcja zwaraca obwiednię sygnału
     */
    public static int[] getEnvelopeTop(int[] x) {
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

    public static int[] getEnvelopeBottom(int[] x) {
        int[] envelope = new int[x.length];


        //p i e są indeksami i opisują obsza między maksimami
        int p = 0;//poczatek
        int e = 0;//koniec
        envelope[0] = x[0];
        for (int i = 1; i < x.length - 1; i++) {
            //szukamy maximum
            if (x[i] <= x[i - 1] && x[i] <= x[i + 1] && x[i] < 0) {
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

    public int[] join(int[] sound, int[] envelopeTop, int[] envelopeBottom) {
        int[] s = new int[sound.length];

        for (int i = 0; i < voice.length; i++) {
            if (sound[i] > envelopeTop[i]) {
                s[i] = envelopeTop[i];
                continue;
            }
            if (sound[i] < envelopeBottom[i]) {
                s[i] = envelopeBottom[i];
                continue;
            }
            s[i] = sound[i];
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
            //Obliczamy obwiednię głosu (w danym paśmie)
            int[] envelopeTop = getEnvelopeTop(v);
            int[] envelopeBottom = getEnvelopeBottom(v);

            //filtrujemy nową nośną
            int[] s = bandPassFilter(sound, lowPassFreq + i * (highPassFreq - lowPassFreq) / passes, lowPassFreq + (i + 1) * (highPassFreq - lowPassFreq) / passes);

            s = join(s, envelopeTop, envelopeBottom);

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


}
