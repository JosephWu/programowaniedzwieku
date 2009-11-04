/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javadaw;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author pioras
 */
public class SoundFileChooserGUI extends JFileChooser {

    private String path;

    public SoundFileChooserGUI() {
        this.addChoosableFileFilter(new SoundFileFilter());
        this.setAcceptAllFileFilterUsed(false);

    }

    public int loadOpenDialog() {
        int returnVal = this.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                System.out.println(this.getSelectedFile().getAbsolutePath());
                this.path = new String(this.getSelectedFile().getAbsolutePath());
            } catch (NullPointerException en) {
                System.out.println("Najpierw musisz wczytać jakiś plik! ");
                JOptionPane.showMessageDialog(null,
                        "Najpierw musisz wczytac jakiś plik! ",
                        "Uwaga! ", JOptionPane.ERROR_MESSAGE);
            }
        }
        return returnVal;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }
}
