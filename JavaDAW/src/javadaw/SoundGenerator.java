/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

/**
 *
 * @author Stach
 */
public class SoundGenerator {

    OneSound soud;

    public SoundGenerator(OneSound soud) {
        this.soud = soud;
    }

    /*
     * Funkcja do generowania różnych typów sygnału:
     * @param 1-sinus, 2-prostokąt, 3-piła, 4-trójkąt
     */
    public int[] generateSound(int type, int length, int frequency, int n) {
        int[] val = new int[length];

        for (int i = 0; i < val.length; i++) {
            val[i] = 0;
        }

        switch (type) {

            case 1://Sinus
                for (int t = 0; t < val.length; t++) {
                    for (int k = 0; k < n; k++) {
                        val[t] += (int) (32767.0 * (Math.sin(2 * Math.PI * t * frequency / 44100)));
                    }
                }
                break;
            case 2://Prostokąt
                for (int t = 0; t < val.length; t++) {
                    for (int k = 0; k < n; k++) {
                        val[t] += (int) (32767.0 / 2 * 4 / Math.PI * (Math.sin(2 * (2 * k - 1) * Math.PI * t * frequency / 44100)) / (2 * k - 1));
                    }
                    System.out.println(val[t]);
                }
                break;
            case 3://Piła
                for (int t = 0; t < val.length; t++) {
                    for (int k = 0; k < n; k++) {
                        val[t] += (int) (32767.0 * 2 / Math.PI * (Math.sin(2 * k * Math.PI * t * frequency / 44100)) / k);
                    }
                }
                break;
            case 4://Trójkąt
                for (int t = 0; t < val.length; t++) {
                    for (int k = 0; k < n; k++) {
                        val[t] += (int) (32767.0 * Math.pow(-1, k) * 8 / (Math.PI * Math.PI) * (Math.sin(2 * (2 * k + 1) * Math.PI * t * frequency / 44100)) / ((2 * k + 1) * (2 * k + 1)));
                    }
                    break;
                }

        }
        return val;
    }
}
