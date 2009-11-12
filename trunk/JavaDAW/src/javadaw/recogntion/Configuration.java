package javadaw.recogntion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Klasa reprezentuje konfigurację dla metod klasyfikacji
 * (KNN, drzewa decyzyjne..)
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class Configuration {

    /**
     * Ilość sąsiadów dla klasyfikacji KNN
     */
    private static int neighboursCount = 1;
    // a potem dla 8,7,5,3,2; narazie dla 10 najlepszy, dla 20 gorszy
    /**
     * Ilość możliwych etykiet, cyfr jest 10 więc 10
     */
    private static int availableLabels = 2;
    /**
     * Stała dla metryki euklidesowej
     */
    public static final int EUCLIDES = 1;
    /**
     * Stała dla metryki ulicznej
     */
    public static final int STREET = 2;
    /**
     * Stała dla metryki Czebyszewa
     */
    public static final int CZEBYSZEW = 3;
    /**
     * Stała dla metryki Minkowskiego
     */
    public static final int MINKOWSKI = 4;
    /**
     * Wybrana metryka
     */
    private static int metric = EUCLIDES;
    /**
     * Ścieżka do pliku, w którym podczas ekstrakcji zostały zapisane dane
     * z największymi wartościami dla poszczególnych cech. Jest wykorzystywana
     * jeśli normalizacja ustawiona jest na true
     */
    public static String maxValuesVectorFile = "data/maxValuesVector.txt";
    /**
     * Stała określająca czy włączona ma zostać normalizacja, dla naszego
     * zadania zawsze true, ze względu na dobre wyniki
     */
    public static boolean normalization = true;
    /**
     * Tablica z maksymalnymi wartościami dla poszczególnych cech,
     * wartości wpisywane są z pliku przygotowanego przez program ekstrkacji
     * cech
     */
    public static double[] maxValues;

    /**
     * @return the metric
     */
    public static int getMetric() {
        return metric;
    }

    /**
     * @param aMetric the metric to set
     */
    public static void setMetric(int aMetric) {
        metric = aMetric;
    }

    /**
     * @return the neighboursCount
     */
    public static int getNeighboursCount() {
        return neighboursCount;
    }

    /**
     * @param aNeighboursCount the neighboursCount to set
     */
    public static void setNeighboursCount(int aNeighboursCount) {
        neighboursCount = aNeighboursCount;
    }

    /**
     * @return the availableLabels
     */
    public static int getAvailableLabels() {
        return availableLabels;
    }

    /**
     * @param aAvailableLabels the availableLabels to set
     */
    public static void setAvailableLabels(int aAvailableLabels) {
        availableLabels = aAvailableLabels;
    }

    /**
     * Metoda ma za zadanie wczytać maksymalne wartości dla cech, potrzebne
     * do normalizacji, do specjalnie przygotowanej w tym celu tablicy
     * maksymalnych wartości typu rzeczywistego dla cech.
     */
    public static void readMaxValuesVector() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(new File(maxValuesVectorFile)));
            String line = input.readLine();
            line = line.replace(";", "");
            Pattern pattern = Pattern.compile(",");
            String[] result = pattern.split(line);
            maxValues = new double[result.length];
            for (int i = 0; i < result.length; i++) {
                maxValues[i] = Double.parseDouble(result[i]);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
