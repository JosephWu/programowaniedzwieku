
package javadaw;

/**
 * Klasa reprezentująca liczbę zespoloną. Ułatwia obliczenia na liczbach
 * zespolonych.
 *
 * @author Stanisław Kacprzak i Michał Piórek
 */
public class Complex {

    /**
     * część rzeczywista
     */
    private double re;

    /**
     * część urojona
     */
    private double im;

    public void setIm(double im) {
        this.im = im;
    }

    public void setRe(double re) {
        this.re = re;
    }



    /**
     * Konstruktor liczby zespolonej
     *
     * @param re część rzeczywista liczby zespolonej
     * @param im część urojona liczby zespolonej
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Metoda pozwala na wyświetlenie liczby zepolonej
     * w odpowiednim formacie.
     *
     * @return string reprezentacja tekstowa liczby zespolonej
     */
    @Override
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /**
     * Moduł z liczby zespolonej.
     *
     * Math.sqrt(re*re + im*im)
     */
    public double abs() {
        return Math.hypot(re, im);
    }


    public double absPower() {
        return Math.hypot(re, im)*Math.hypot(re, im);
    }

    /**
     * Faza liczby zespolonej.
     *
     * między -pi a pi
     */
    public double phase() {
        return Math.atan2(im, re);
    }

    /**
     * Dodawanie liczb zespolonych.
     *
     * @param b liczba zespolona do dodania
     * @return wynik dodawania liczb zespolonych
     */
    public Complex plus(Complex b) {
        double reToRet = this.re + b.re;
        double imToRet = this.im + b.im;
        return new Complex(reToRet, imToRet);
    }

    /**
     * Odejmowanie liczb zespolonych.
     *
     * @param b liczba zespolona do odjęcia od liczby/obiektu,
     * na rzecz, którego została wywołana metoda
     * @return wynik odejmowania liczb zespolonych
     */
    public Complex minus(Complex b) {
        double reToRet = this.re - b.re;
        double imToRet = this.im - b.im;
        return new Complex(reToRet, imToRet);
    }

    /**
     * Mnożenie liczb zespolonych.
     *
     * @param b liczba zespolona do pomnożenia liczby/obiektu,
     * na rzecz, którego została wywołana metoda
     * @return wynik mnożenia liczb zespolonych
     */
    public Complex times(Complex b) {
        Complex a = this;
        double reToRet = a.re * b.re - a.im * b.im;
        double imToRet = a.re * b.im + a.im * b.re;
        return new Complex(reToRet, imToRet);
    }

    /**
     * Mnożenie liczby zespolonej przez skalar/wartość.
     *
     * @param alpha wartość typu rzeczywistego (double),
     * przez którą należy pomnożyć liczbe zespoloną, na rzecz której
     * wywołano metodę
     * @return wynik mnożenia liczby zespolonej przez skalar
     */
    public Complex times(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /**
     * Sprzężenie liczby zespolonej (ang. conjugate).
     *
     * @return sprzężenie liczby zespolonej
     */
    public Complex conjugate() {
        return new Complex(re, -im);
    }


    /**
     * Element odwrotny mnożenia (odwrotność) dla dowolnej
     * niezerowej liczby zespolonej (ang. reciprocal).
     *
     * @return odwrotność liczby zespolonej w sensie mnożenia
     */
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    /**
     * Metoda pozwala pobrać rzeczywistą część liczby zespolonej.
     *
     * @return dobule rzeczywista część liczby zespolonej
     */
    public double getRe() {
        return this.re;
    }

    /**
     * Metoda pozwala pobrać urojoną część liczby zespolonej.
     *
     * @return double urojona część liczby zespolonej
     */
    public double getIm() {
        return this.im;
    }

    // return a / b
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im),
                Math.exp(re) * Math.sin(im));
    }

    // return a new Complex object whose value is the complex sine of this
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im),
                Math.cos(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex cosine of this
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im),
                -Math.sin(re) * Math.sinh(im));
    }

    // return a new Complex object whose value is the complex tangent of this
    public Complex tan() {
        return sin().divides(cos());
    }

    /**
     * Statyczna wersja metody pozwalającej na dodawanie liczb zespolonych.
     *
     * @param a pierwsza liczba zespolona do dodania
     * @param b druga liczba zespolona do dodania
     * @return wynik dodawania liczb zespolonych
     */
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }

}
