
package javadaw;

/**
 * Klasa Mixer reprezentuje "mikser", do którego podawane są kolejne dźwięki/
 * sygnały poprzez metodę addSignal(). Obiekt tej klasy ma za zadanie dodać
 * do siebie dźwięki.
 */
public class Mixer {

    /**
     * Przetwarzany sygnał
     */
    private int[] signal;

    /**
     * Metoda inicjalizująca mixer, tutaj musi zostać podany pierwszy sygnał
     * do mixera. Wywołuj tą metodę tylko raz dla danego sygnału dźwiękowego.
     * Jeśli chcesz wykorzystać mixer do następnego dźwięku to możesz wywołać
     * tą metodę po raz kolejny.
     *
     * @param values pierwszy sygnał podany do mixera w postaci tablicy int
     */
    public void putSignal(int[] values) {
        signal = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            signal[i] = values[i];
        }
    }

    /**
     * Metoda ma za zadanie dodać do przetwarzanego sygnału kolejny sygnał
     * podany jako parametr metody.
     *
     * @param values dodawany sygnał
     */
    public void addSignal(int[] values) {
        for (int i = 0; i < signal.length; i++) {
            signal[i] = (signal[i] + values[i]) / 2;
        }
    }

    /**
     * Metoda ma za zadanie zwrócić przetworzony sygnał.
     *
     * @return przetworzony sygnał
     */
    public int[] getOutput() {
        return signal;
    }

}
