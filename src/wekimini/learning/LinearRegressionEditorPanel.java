/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekimini.learning;

import java.util.logging.Logger;

/**
 *
 * @author rebecca
 */
public class LinearRegressionEditorPanel extends ModelBuilderEditorPanel {
    private static final Logger logger = Logger.getLogger(LinearRegressionEditorPanel.class.getName());
    /**
     * Creates new form LinearRegressionEditorPanel
     */
    public LinearRegressionEditorPanel() {
        initComponents();
    }
    
    public LinearRegressionEditorPanel(LinearRegressionModelBuilder mb) {
        initComponents();
        if (mb.getFeatureSelectionType() == LinearRegressionModelBuilder.FeatureSelectionType.NONE) {
            comboSelectionType.setSelectedIndex(0);
        } else if (mb.getFeatureSelectionType() == LinearRegressionModelBuilder.FeatureSelectionType.M5) {
            comboSelectionType.setSelectedIndex(1);
        } else {
            //Greedy
            comboSelectionType.setSelectedIndex(2);
        }
    }
    
    @Override
    public boolean validateForm() {
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        comboSelectionType = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Feature selection method:");

        comboSelectionType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "M5", "Greedy" }));
        comboSelectionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSelectionTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(comboSelectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboSelectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 292, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 51, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void comboSelectionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSelectionTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboSelectionTypeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboSelectionType;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    //Assumes form is valid
    @Override
    public LinearRegressionModelBuilder buildFromPanel() {
        LinearRegressionModelBuilder.FeatureSelectionType fs;
        if (comboSelectionType.getSelectedIndex() == 0) {
            fs = LinearRegressionModelBuilder.FeatureSelectionType.NONE;
        } else if (comboSelectionType.getSelectedIndex() == 1) {
            fs = LinearRegressionModelBuilder.FeatureSelectionType.M5;
        } else {
            fs = LinearRegressionModelBuilder.FeatureSelectionType.GREEDY;
        }
        LinearRegressionModelBuilder mb = new LinearRegressionModelBuilder();
        mb.setFeatureSelectionType(fs);
        return mb;
    }
}