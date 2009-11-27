
package javadaw;

/**
 *
 */
public class SoundGenerator {

    public SoundGenerator() {
    }

    /*
     * Funkcja do generowania różnych typów sygnału:
     * @param 1-sinus, 2-prostokąt, 3-piła, 4-trójkąt
     */
    public int[] generateSound(int type, int length, int frequency, int n) {
        int[] val = new int[length];
        double[] tmpVals = new double[length];
        double max = 0;
        for (int t = 0; t < length; t++) {
            double tmp = 0;
            switch (type) {
                case 1:
                    //Sinus
                    tmp = (Math.sin(2.0*Math.PI*(double)t*(double)frequency/44100.0));
                    break;
                case 2:
                    //Prostokąt
                    for (int k = 1; k < n; k++) {
                        tmp += (Math.sin((2.0*k-1)*2.0*Math.PI*(double)frequency*(double)t/44100.0)/(2.0*k-1));
                    }
                    break;
                case 3:
                    //Piła
                    for (int k = 1; k < n; k++) {
                        tmp += ((Math.sin(2.0*(double)k*Math.PI*(double)frequency*(double)t/44100.0))/(double)k);
                    }
                    break;
                case 4:
                    //Trójkąt
                    for (int k = 0; k < n; k++) {
                        tmp += ((Math.pow(-1, k)*(Math.sin(2.0*Math.PI*(2.0*k+1)*(double)frequency*(double)t/44100.0))/((2.0*k+1)*(2.0*k+1))));
                    }
                    break;
            }
            tmpVals[t] = tmp;
            if (tmp > max)
                max = tmp;
        }
        for (int t = 0; t < length; t++) {
            val[t] = (int) (tmpVals[t] / max * 32767.0);
        }
        return val;
    }
}
