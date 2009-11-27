
package javadaw;

/**
 * Klasa reprezentuje wzmacniacz. Ma za zadanie tak znormalizować sygnały, aby
 * miały poprawną głośność np. w Mixer dodajemy do siebie dwa sygnały i dzielimy
 * przez 2, przez co jak dodamy sygnał i ciszę to otrzymamy sygnał o głośności
 * dwa razy mniejszej od oryginały, musimy zatem na końcu to normalizować
 * wzmacniaczem.
 */
public class Ampli {

    /**
     * Normalizuj sygnał do pożadanej maksymalnej głośności. Wzmacniacz sygnału.
     *
     * @param values wejściowy sygnał do normalizacji
     * @return tablica z wartościami int z sygnałem znormalizowanym
     */
    int[] normalizeSingal(int[] values) {
        int max = 0; int min = 0;
        for (int i = 0; i < values.length; i++) {
            if (max < values[i]) max = values[i];
            if (min > values[i]) min = values[i];
        }
        double ratioMin = (double)min / -32767.0;
        double ratioMax = (double)max / 32767.0;
        double ratio = 0;
        if (ratioMin > ratioMax)
            ratio = ratioMin;
        else
            ratio = ratioMax;
        for (int i = 0; i < values.length; i++) {
            values[i] = (int) ((double)values[i] / ratio);
        }
        return values;
    }

}
