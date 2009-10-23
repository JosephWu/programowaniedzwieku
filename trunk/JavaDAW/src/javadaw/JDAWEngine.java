/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

/**
 *
 * @author pioras
 */
public class JDAWEngine {

    public DeviceInfo deviceInfo;
    private JavaDAWView javaDAWView;

    public JDAWEngine(JavaDAWView javaDAWView) {
        this.javaDAWView = javaDAWView;
        this.deviceInfo = new DeviceInfo(javaDAWView);
    }

}
