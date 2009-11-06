
package javadaw.recogntion;

/**
 * Klasa reprezentuje podobieństwa między kolejnymi przypadkami.
 * Przykłądowo określa na ile dany obrazek jest podobny do kolejnego.
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class Distance implements Comparable<Distance> {

    /**
     * Miara podobieństwa/dystansu
     */
    private Double distance;

    /**
     * Etykieta dla danego dystansu
     */
    private int label;

    /**
     * Konstruktor tworzący nowy dystans między przypadkami.
     *
     * @param distance podobieństwo dwóch porównywanych przypadków
     * @param label etykieta przypadku treningowego, z którym porównywany jest
     * przypadek testowy
     */
    public Distance(Double distance, int label) {
        this.distance = distance;
        this.label = label;
    }

    /**
     * Komparator pozwalający łatwo sortować kolekcję Dystansów
     * @param o dystans do porównania
     * @return -1 w przypadku, gdy porównywany dystans jest mniejszy, zero
     * w przypadku gdy są równe i 1 w przypadku gdy dystans porównywany jest
     * większy, od tego, na rzecz którego został wywołany komparator
     */
    public int compareTo(Distance o) {
        if (o.getDistance() < this.distance)
            return -1;
        else if (o.getDistance() == this.distance)
            return 0;
        else 
            return 1;
    }

    /**
     * @return the distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(Double distance) {
        this.distance = distance;
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

    

}
