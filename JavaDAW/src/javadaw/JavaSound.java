
package javadaw;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Klasa reprezentuje dźwięk dla natywnego Java API do generowania dźwięków
 * a potem do filtracji efektów itd. do wokodera też...
 */
public class JavaSound {

    private AudioFormat audioFormat;
    private SourceDataLine line;

    public int frequency = 44100;
    public int sampleSizeBits = 16;
    public int numberOfChannels = 1;
    public boolean bigEndian = true;
    public boolean isSigned = true;

    /**
     * Sygnał dźwięki w postaci tablicy bajtów (od -127 do 127)
     */
    private byte[] soundData;

    public JavaSound() {

    }

    /**
     * Metoda inicjalizująca dźwięk. Jeśli tworzysz pusty dźwięk to wywołaj
     * tą metodę od razu po utworzeniu obiektu tej klasy.
     */
    public void createSound() {
        try {
            audioFormat = new AudioFormat(frequency, sampleSizeBits, numberOfChannels, isSigned, bigEndian);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(JavaSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda pozwala odtworzyć dźwięk.
     */
    public void playSound() {
        try {
            line.open();
            line.start();
            {
                line.write(soundData, 0, soundData.length);
            }
        } catch (LineUnavailableException ex) {
            Logger.getLogger(JavaSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda pozwala zatrzymać dźwięk.
     */
    public void stopSound() {
        line.stop();
        line.drain();
        line.close();
    }

    /**
     * Metoda zmienia sygnał dźwiękowy z postaci tablicy int na tablicę byte
     * dla 16bit próbek.
     *
     * @param values sygnał dźwiękowy w postaci tablicy int od -32k do +32k
     */
    private void intToBytes(int[] values) {
        this.soundData = new byte[values.length * (this.sampleSizeBits / 8)];
        for (int i = 0; i < values.length; i++) {
            soundData[2*i] = (byte) (((values[i] >> 8) & 0x000000FF));
            soundData[2*i + 1] = (byte) (values[i] & 0x000000FF);
        }
    }

    /**
     * Metoda pozwala załadować dane do pustego, utworzonego wcześniej dźwięku.
     * Długość dźwięku teterminowana jets na podstawie długości podanej tablicy
     * (jako argument metody)
     *
     * @param values sygnał do wstawienia w postaci tablicy int od -32k do +32k
     */
    public void putIntData(int[] values) {
        this.intToBytes(values);
    }

}
