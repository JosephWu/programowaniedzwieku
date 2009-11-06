
package javadaw.recogntion;

import java.util.ArrayList;

/**
 * Klasa reprezentuje wektor cech dla przypadku (obrazka, kwiatka... w naszym
 * przypadku)
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class FeaturesVector {

    /**
     * Lista cech typu rzeczywistego
     */
    private ArrayList<Double> features;

    /**
     * Pozycja w liście cech - należy pobierać cech do momentu aż nie zostaną
     * pobrane wszystkie a następnie wyzerować licznik, aby można było użyć
     * ten sam obiekt np. ze zbioru uczącego dla kolejnego przypadku ze zbioru
     * testowego.
     */
    private int position;

    /**
     * Konstruktor tworzący wektor cech, tutaj pozycji zapisywana jest jako
     * zero oraz tworzona jest, narazie pusta, lista cech typu rzeczywistego.
     */
    public FeaturesVector() {
        this.features = new ArrayList<Double>();
        this.position = 0;
    }

    /**
     * Dodaje cechę typu rzeczywistego do listy cech.
     *
     * @param value wartość cechy, liczba rzeczywista
     */
    public void addFeature(Double value) {
        this.features.add(value);
    }

    /**
     * Metoda pobiera kolejną cechę z listy cech dla przypadku.
     * Jeśli nie ma już więcej cech to zwracana jest wartość null a licznik
     * ustawiany jest spowrotem na pozycję 0.
     *
     * @return liczba rzeczywista reprezentująca cechę
     */
    public Double getNextFeature() {
        if (this.position < this.features.size()) {
            Double toRet = this.features.get(this.position);
            this.position++;
            return toRet;
        } else {
            this.position = 0;
            return null;
        }
    }

    /**
     * Metoda ma za zadanie obliczyć różnicę/odległość między wektorami cech.
     *
     * @param fv wektor, z którym ma zostać porównany wektor, na rzecz, którego
     * wykonywana jest ta metoda
     * @return wartość rzeczywista określająca podobieństwo wektora cech
     */
    public Double showDiff(FeaturesVector fv) {
        int metric = Configuration.getMetric();
        Double value1 = null;
        Double value2 = null;
        Double difference = 0.0;
        while ((value1 = this.getNextFeature()) != null) {
            value2 = fv.getNextFeature();
            switch(metric) {
                case Configuration.EUCLIDES :
                    difference += (value1 - value2)*(value1 - value2) ;
                    break;
                case Configuration.STREET :
                    difference += Math.abs(value1 - value2);
                    break;
                case Configuration.CZEBYSZEW :
                    if (difference < Math.abs(value1 - value2))
                        difference = Math.abs(value1 - value2);
                    break;
                case Configuration.MINKOWSKI :
                    double tmp = Math.abs(value1 - value2);
                    difference += tmp*tmp*tmp;
                    break;
            }
        }
        fv.getNextFeature();
        switch (metric) {
            case Configuration.EUCLIDES:
                return Math.sqrt(difference);
            case Configuration.MINKOWSKI:
                return Math.pow(difference, (1.0/3.0));
            case Configuration.STREET:
            case Configuration.CZEBYSZEW:
            default:
                return difference;
        }
    }

}
