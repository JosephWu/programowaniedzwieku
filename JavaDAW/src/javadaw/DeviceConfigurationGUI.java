/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DeviceConfigurationGUI.java
 *
 * Created on 2009-10-23, 23:51:11
 */

package javadaw;

/**
 *
 * @author pioras
 */
public class DeviceConfigurationGUI extends javax.swing.JFrame {

    private JDAWEngine jDAWEngine;

    /** Creates new form DeviceConfigurationGUI */
    public DeviceConfigurationGUI(JDAWEngine jDAWEngine) {
        this.jDAWEngine = jDAWEngine;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        devicesComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        driversComboBox = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        recordDevicesComboBox = new javax.swing.JComboBox();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(javadaw.JavaDAWApp.class).getContext().getResourceMap(DeviceConfigurationGUI.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        devicesComboBox.setName("devicesComboBox"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        driversComboBox.setName("driversComboBox"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        recordDevicesComboBox.setName("recordDevicesComboBox"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(devicesComboBox, 0, 248, Short.MAX_VALUE)
                        .addComponent(driversComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addComponent(recordDevicesComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(devicesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(driversComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordDevicesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.jDAWEngine.deviceInfo.getSystem().release();
        this.jDAWEngine.deviceInfo.createSystem();
        this.jDAWEngine.deviceInfo.setDevice(this.getDevicesComboBox().getSelectedIndex());
        this.jDAWEngine.deviceInfo.setDriver(this.getDriversComboBox().getSelectedIndex());
        this.jDAWEngine.recordDeviceInfo.setDevice(this.getRecordDevicesComboBox().getSelectedIndex());
        this.jDAWEngine.deviceInfo.init();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox devicesComboBox;
    private javax.swing.JComboBox driversComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox recordDevicesComboBox;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the jComboBox1
     */
    public javax.swing.JComboBox getDevicesComboBox() {
        return devicesComboBox;
    }

    /**
     * @param jComboBox1 the jComboBox1 to set
     */
    public void setDevicesComboBox(javax.swing.JComboBox devicesComboBox) {
        this.devicesComboBox = devicesComboBox;
    }

    /**
     * @return the jComboBox2
     */
    public javax.swing.JComboBox getDriversComboBox() {
        return driversComboBox;
    }

    /**
     * @param jComboBox2 the jComboBox2 to set
     */
    public void setjComboBox2(javax.swing.JComboBox driversComboBox) {
        this.driversComboBox = driversComboBox;
    }

    /**
     * @return the recordDevicesComboBox
     */
    public javax.swing.JComboBox getRecordDevicesComboBox() {
        return recordDevicesComboBox;
    }

}