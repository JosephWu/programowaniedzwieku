
package javadaw.recogntion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Klasa została stworzona na potrzeby uporządkowania kodu pomocniczego,
 * w tym wypadku klasa odpowiedzialna jest za wczytanie wszystkich
 * przypadków ze wskazanych plików tekstowych w odpowiednim formacie.
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class LoadTxt {

    /**
     * Konstruktor klasy odpowiedzialny za wczytanie plików tekstowych
     * ze wskazanej ścieżki.
     *
     * @param path ścieżka do plików tekstowych z informacją o zbiorze
     * @return lista wczytanych z pliku przypadków
     */
    public ArrayList<Case> loadCasesFromTxt(String path) {
        File file = new File(path);

        //Wczytuje z pliku wartość wektora maksymalnych wartość cech\
        //potrzebnego do normalizacji cech


        ArrayList<Case> cases = new ArrayList<Case>();
        // 71,30,41,39,32,0,15;7:114,60,54,50,64,5,27;2...
        try {
            //FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(file));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    StringBuilder content = new StringBuilder();
                    content.append(line);
                    Case oneCase = new Case();

                    // Stworzenie wzorców
                    // 71,30,41,39,32,0,15 oraz 7
                    Pattern pattern = Pattern.compile(";");
                    // 71
                    Pattern subPattern = Pattern.compile(",");
                    String[] result = pattern.split(content);

                    for (int i = 0; i < result.length; i++) {
                        String[] subResult = subPattern.split(result[i]);
                        if (i == result.length - 1) {
                            oneCase.setLabel(Integer.valueOf(subResult[0]));
                        } else {
                            //Jeżlei wybrana została normalizacjo to zadziała ona wtym momencie
                            if (Configuration.normalization) {
                                for (int j = 0; j < subResult.length; j++) {
                                    oneCase.addFeature(Double.valueOf(subResult[j]));
                                }
                            } else {
                                for (int j = 0; j < subResult.length; j++) {
                                    oneCase.addFeature(Double.valueOf(subResult[j]));
                                }
                            }
                        }
                    }

                    cases.add(oneCase);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cases;
    }

}
