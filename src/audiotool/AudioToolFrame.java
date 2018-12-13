/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotool;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author MSi-Gaming
 */
public class AudioToolFrame extends JFrame {

    private List<File> files;
    private DefaultListModel<String> model;
    private DefaultListModel<String> modelProcess;
    private AudioTool audioTool;
    private int success, fail;
    private ConvertAssToLrc mConvertAssToLrc;

    public AudioToolFrame() {
        initComponents();
        centerFrame();
        setTitle("AudioTool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initArgument();
    }

    private void initArgument() {
        files = new ArrayList<File>();
        model = new DefaultListModel<>();
        modelProcess = new DefaultListModel<>();
        lvFilesChoose.setModel(model);
        lvFileProcess.setModel(modelProcess);
        btnProcess.setEnabled(false);
        audioTool = new AudioTool();
        mConvertAssToLrc = new ConvertAssToLrc();
    }

    private void centerFrame() {
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        setLocation(dx, dy);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tvBrowser = new javax.swing.JLabel();
        btnBrowser = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lvFilesChoose = new javax.swing.JList<>();
        prgFileStatus = new javax.swing.JProgressBar();
        tvStatus = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lvFileProcess = new javax.swing.JList<>();
        btnProcess = new javax.swing.JButton();
        btnLyric = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tvBrowser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tvBrowser.setName("tvBrowser"); // NOI18N

        btnBrowser.setText("Duyệt");
        btnBrowser.setName("btnBrowser"); // NOI18N
        btnBrowser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBrowserMouseClicked(evt);
            }
        });
        btnBrowser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowserActionPerformed(evt);
            }
        });

        lvFilesChoose.setName("lvFilesChoose"); // NOI18N
        jScrollPane1.setViewportView(lvFilesChoose);

        prgFileStatus.setName("prgFileProcess"); // NOI18N

        tvStatus.setText("0/1000");
        tvStatus.setName("tvFilesStatus"); // NOI18N

        jScrollPane2.setViewportView(lvFileProcess);

        btnProcess.setText("Xử lý");
        btnProcess.setName("btnProcess"); // NOI18N
        btnProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProcessMouseClicked(evt);
            }
        });

        btnLyric.setText("Lyric");
        btnLyric.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLyricMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tvStatus)
                        .addGap(0, 831, Short.MAX_VALUE))
                    .addComponent(prgFileStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tvBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLyric, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tvBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnProcess, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                        .addComponent(btnBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                    .addComponent(btnLyric, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(prgFileStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tvStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBrowserActionPerformed

    private void btnBrowserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowserMouseClicked
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        FileFilter audioFilter = new FileNameExtensionFilter("Audio files", "mp3", "mid");
        fileChooser.setFileFilter(audioFilter);
        fileChooser.showOpenDialog(getMostRecentFocusOwner());
        if (fileChooser.getSelectedFiles().length == 0) {
            return;
        }
        System.out.println("Choosed");
        files.clear();
        files.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
        model.clear();
        for (File file : files) {
            tvBrowser.setText(file.getParent());
            model.addElement(file.getName());
        }
        tvStatus.setText(0 + "/" + files.size());
        prgFileStatus.setMaximum(files.size());
        prgFileStatus.setValue(0);
        btnProcess.setEnabled(true);
    }//GEN-LAST:event_btnBrowserMouseClicked

    private void btnProcessMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProcessMouseClicked
        // TODO add your handling code here:
        btnProcess.setEnabled(false);
        btnBrowser.setEnabled(false);
        modelProcess.clear();
        prgFileStatus.setValue(0);
        success = fail = 0;
        tvStatus.setText(0 + "/" + files.size());
//        audioTool.processAudioFiles(files, new AudioTool.OnAudioProcessListener() {
//            @Override
//            public void onSuccess(String file) {
//                modelProcess.addElement(file);
//                prgFileStatus.setValue(modelProcess.size());
//                tvStatus.setText(modelProcess.size() + "/" + files.size());
//                success++;
//                if (modelProcess.size() == files.size()) {
//                    tvStatus.setText("Thành công: " + success + ", Thất bại: " + fail);
//                    btnBrowser.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                modelProcess.addElement(msg);
//                prgFileStatus.setValue(modelProcess.size());
//                tvStatus.setText(modelProcess.size() + "/" + files.size());
//                fail++;
//                if (modelProcess.size() == files.size()) {
//                    tvStatus.setText("Thành công: " + success + ", Thất bại: " + fail);
//                    btnBrowser.setEnabled(true);
//                }
//            }
//        });
        audioTool.processFilesMIDI(files);


    }//GEN-LAST:event_btnProcessMouseClicked

    private void btnLyricMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLyricMouseClicked
        // TODO add your handling code here:
        if (!mConvertAssToLrc.isVisible()) {
            mConvertAssToLrc.setVisible(true);
        }
    }//GEN-LAST:event_btnLyricMouseClicked

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AudioToolFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowser;
    private javax.swing.JButton btnLyric;
    private javax.swing.JButton btnProcess;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lvFileProcess;
    private javax.swing.JList<String> lvFilesChoose;
    private javax.swing.JProgressBar prgFileStatus;
    private javax.swing.JLabel tvBrowser;
    private javax.swing.JLabel tvStatus;
    // End of variables declaration//GEN-END:variables
}
