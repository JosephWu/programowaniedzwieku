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
            if (x[i] >= x[i - 1] && x[i] >= x[i + 1] && x[i]>0) {
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
}
