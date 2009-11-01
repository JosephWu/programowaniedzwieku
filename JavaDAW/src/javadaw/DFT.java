
package javadaw;

/**
 * Klasa pozwalająca wykonywać proste i odwrotne przekształcenie
 * Fouriera Radix-2 DIT (decymacja w dziedzinie czasu).
 * Wykorzystuje rekurencje.
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class DFT {
    
    public DFT() {
    }

    /**
     * Metoda oblicza szybką transformatę Fouriera
     *
     * @param data dane w postaci liczb zespolonych
     * @return obliczona transformata sygnału
     */
    public Complex[] fft(Complex[] data) {

        // Ilość elementów
        int N = data.length;
        // najprostszy przypadek kończący rekurencję
        if (N == 1) return new Complex[] { data[0] };
        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) {
            System.err.println("Dan nie są " +
                    "potęgą dwójki!");
            return null;
        }

        // FFT na nieparzystych elementach
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = data[2*k];
        }
        Complex[] l = fft(even);

        // FFT na parzystych elementach
        Complex[] odd  = even;
        for (int k = 0; k < N/2; k++) {
            odd[k] = data[2*k + 1];
        }
        Complex[] r = fft(odd);

        // Zespalanie obliczonych transformat ze sobą
        Complex[] output = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            output[k]       = l[k].plus(wk.times(r[k]));
            output[k + N/2] = l[k].minus(wk.times(r[k]));
        }
        return output;
    }


    /**
     * Metoda oblicza szybką odwrotną transformatę Fouriera.
     *
     * @param data dane w postaci liczb zespolonych
     * @return dane po odwrotnej transformacie
     */
    public Complex[] ifft(Complex[] data) {
        
        // Ilość elementów
        int N = data.length;
        
        Complex[] output = new Complex[N];

        // sprzęzenie liczby zespolonej, po wszystkich elementach
        for (int i = 0; i < N; i++) {
            output[i] = data[i].conjugate();
        }

        // obliczenie zwykłej FFT
        output = fft(output);

        // sprzęzenie liczby zespolonej, po wszystkich elementach, ponownie
        for (int i = 0; i < N; i++) {
            output[i] = output[i].conjugate();
        }

        // Dzielenie przez ilość elementów w transformacie
        // zgodnie z wzorami dla zachowania przekształcenia.
        for (int i = 0; i < N; i++) {
            output[i] = output[i].times(1.0 / N);
        }

        return output;
    }

    public double[] getSpectrum(Complex[] data) {
        double[] spectrumLine = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            spectrumLine[i] = data[i].abs();
        }
        return spectrumLine;
    }


}
