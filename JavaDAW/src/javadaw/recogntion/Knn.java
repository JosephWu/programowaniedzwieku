
package javadaw.recogntion;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Klasa KNN reprezentuje metodę klasyfikacji o tej samej nazwie, czyli
 * główną metodę zadania, którą należało zaimplementować samodzielnie
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class Knn {

    /**
     * Lista przypadków treningowych np. obrazków (ich cech włącznie z etykietą)
     */
    private ArrayList<Case> trainCases=new ArrayList();

    /**
     * Lista przypadków testowych
     */
    private ArrayList<Case> testCases=new ArrayList();

    /**
     * Lista podobieństw danego przypadku testowego, do wszystkich przypadków
     * treningowych
     */
    private ArrayList<Distance> distances;

    /**
     * Liczba poprawnych odpowiedzi KNN
     */
    private int correctAnswers = 0;

    /**
     * kod tymczasowy, tylko dla liczb
     * Tablica ma przechowywać informację o tym ile razy dana
     * cyfra nie została rozpoznana, indeks jest etykietą cyfry
     */
    int[] valueMissed = new int[10];

    /**
     * Główna metoda KNN uruchamiająca algorytm, tutaj porównywany jest każdy
     * przypadek testowy ze wszystkimi treningowymi.
     */
    public int run() {
    
            this.distances = new ArrayList<Distance>();
            for (int j = 0; j < trainCases.size(); j++) {
                /**
                 * i - dla pierwszego testowego przypadku,
                 * od razu rozpatrzymy wszystkie, wiec tablica dwu
                 * wymiarowa, j to bedzie ilosc elementow treningowych,
                 * bo z nimi porownujemy
                 */
                
                this.distances.add(new Distance(testCases.get(0).getDiff(
                        trainCases.get(j).getFeatures()),
                        trainCases.get(j).getLabel()));
            }
            testCases.clear();
            return this.chooseBest(0);

    }

   /**
    * Metoda ma za zadanie zdecydować jaką cyfrą jest rozpoznawana cyfra
    * ze zbioru testowego.
    *
    * @param testCaseNumber numer przypadku testowego, ważny ze względu na fakt
    * konieczności pobrania odpowiedniego przypadku z listy, metoda wywoływana
    * jest dla każdego przypadku testowego,
    * ze względu na zasobożerność (pamięci)
    */
    public int chooseBest(int testCaseNumber) {
        Collections.sort(this.distances);
        Collections.reverse(this.distances);

        int[] labelsAmounts = new int[Configuration.getAvailableLabels()];

        for (int i = 0; i < Configuration.getNeighboursCount(); i++) {
            labelsAmounts[this.distances.get(i).getLabel()-1]++;
        }
        int superLabel = labelsAmounts[0];
        int finalLabel = 0;
        for (int i = 1; i < labelsAmounts.length; i++) {
            if (labelsAmounts[i] > superLabel) {
                finalLabel = i;
                superLabel = labelsAmounts[i];
            }
        }
        return finalLabel;
    }



    /**
     * @param trainCases the trainCases to set
     */
    public void setTrainCases(ArrayList<Case> trainCases) {
        this.trainCases = trainCases;
    }

    public void addTrainCase(Case case1){
        trainCases.add(case1);
    }
    public void addTestCase(Case case1){
        testCases.add(case1);
    }

    /**
     * @param testCases the testCases to set
     */
    public void setTestCases(ArrayList<Case> testCases) {
        this.testCases = testCases;
    }




}
