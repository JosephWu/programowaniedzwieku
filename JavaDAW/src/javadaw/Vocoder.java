/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

import java.util.Arrays;

/**
 * Ale człowieku w tej klasie to jest bałagan kosmiczny :DDDDDDDDDDDDDDD
 * Ja sie pytam po co są konstruktory? A no po to, zebym ja mogl jak czlowiek
 * utworzyć nowy obiekt i zlukać sobie co tu sie dzieje, a tak zaraz sam dopisze
 * domyslając sie. Poza tym znowu nie dziala Twoj super klient svn
 * bo zapewne nie wcommitowales interfejsu - trudno, ja docommitowalem to co zrobilem
 * Czyli wykonalismy dwa razy ta sama robote.... ja pitole :))))))))))))))))))
 *
 * @author Stach
 */
public class Vocoder {

    /**
     * Wartość reoznansu - do filtrów
     *
     * !!! Komentarz do zmiennych również fajnie robić w tym szybkim komentowaniu
     * czyli / i dwie gwiazdki i enter, natomiast double zapisywac 1.0
     * zamiast 1, poniewaz jest to niemylące, podkreslające tego doubla,
     * ułatwia prace
     */
    private double q = 1.0;

    /**
     * Dolne krańcowe pasmo, w którym działa wokoder
     */
    private int lowPassFreq = 20;

    /**
     * Górne krańcowe pasmo, w którym działa wokoder
     */
    private int highPassFreq = 10000;

    /**
     * liczba rozpatrywanych pasm
     */
    private int passes = 32;

    /**
     * Dzwiek oneSound - w ogóle tu niepotrzebny, nie dobierajmy się do silnika,
     * tu robimy operacje matematyczne tylko czyli tablice intów
     */
    OneSound oneSound;

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
        //p i e są indeksami i opisują obsza między maksimami
        // Czemu P i E? może start? end?
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


    /**
     *
     * @param x
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
     * Uzupełnij komentarz ok? I używaj skrótu do formatowania kodu, bo się
     * tym chwaliłeś i nadal nie używasz, chociaż jest lepiej :)... tak wiem
     * kod pisany na szybko i w ogóle, ale trace czas, który mógłbym
     * poświęcić na BRACHISTOCOŚTAM
     *
     * @param sound
     * @param envelope
     * @return
     */
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
            int lowFrequ = lowPassFreq + i * (highPassFreq - lowPassFreq) / passes;
            int highFreq = lowPassFreq + (i + 1) * (highPassFreq - lowPassFreq) / passes;
            int[] v = bandPassFilter(voice, lowFrequ, highFreq);

            // CO TO JEST TA PĘTLA?
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
     *
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

    
    /**
     * Funkcja tylko testująca wewnątrz programu - domyśliłem się ;)
     * Rób komentarze do nietypowych rzeczy a najlepiej do wszystkich, jeśli
     * robimy razem.
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
