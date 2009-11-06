
package javadaw.recogntion;


/**
 * Klasa reprezentuje pojedynczy przypadek. Dla potrzeb naszego zadania
 * przypadkiem są kwiaty (dla zbioru pierwszego z plikami xml) lub cyfry
 * (dla zbioru właściwego, dla którego przeprowadzane są wszystkie badania)
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class Case {

    /**
     * Wektor cech
     */
    private FeaturesVector features;

    /**
     * Etykieta przypadku
     */
    private int label;

    /**
     * Konstruktor przypadku, tu tworzony jest nowy wektor cech,
     * narazie pusty
     */
    public Case() {
        this.features = new FeaturesVector();
    }

    /**
     * Metoda ma za zadanie dodać nową cechę
     *
     * @param value cecha o wartości rzeczywistej
     */
    public void addFeature(Double value) {
        this.getFeatures().addFeature(value);
    }

    /**
     * Metoda pobiera kolejną cechę dla wybranego przypadku
     *
     * @return cecha typu rzeczywistego, null w przypadku, gdy nie ma już
     * żadnej cechy do pobrania
     */
    public Double getNextFeature() {
        return this.getFeatures().getNextFeature();
    }

    /**
     * Porównanie dwóch wektorów cech, jednego należącego do obiektu,
     * na rzeczy którego wywoływana jest metod i drugiego (wektora), który jest
     * podawany jako parametr
     *
     * @param fv wektor cech do porównania
     * @return wartość rzeczywista określająca stopień podobieństwa na podstawie
     * wybranej metryki lub euklidesowej jeśli nie została określona
     */
    public Double getDiff(FeaturesVector fv) {
        return this.features.showDiff(fv);
    }

    /**
     * @return the label
     */
    public int getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(int label) {
        this.label = label;
    }

    /**
     * @return the features
     */
    public FeaturesVector getFeatures() {
        return features;
    }

}
