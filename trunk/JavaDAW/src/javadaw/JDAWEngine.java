/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javadaw;

import org.jouvieje.FmodEx.System;

/**
 *
 * @author pioras
 */
public class JDAWEngine {

    public DeviceInfo deviceInfo;
    public RecordDeviceInfo recordDeviceInfo;
    private JavaDAWView javaDAWView;
    private System system;

    public JDAWEngine(JavaDAWView javaDAWView) {
        this.javaDAWView = javaDAWView;
        this.deviceInfo = new DeviceInfo(javaDAWView);
        this.system = deviceInfo.getSystem();
        this.recordDeviceInfo = new RecordDeviceInfo(javaDAWView, this);
    }

    public void setDriver(int no) {
        this.deviceInfo.setDriver(no);
    }

    /**
     * @return the system
     */
    public System getSystem() {
        return system;
    }

}
