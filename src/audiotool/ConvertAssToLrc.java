/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotool;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author MSi-Gaming
 */
public class ConvertAssToLrc extends javax.swing.JFrame {

    private DefaultListModel<String> model;
    private DefaultListModel<String> modelProcess;
    private List<File> files;
    private LyricTool mLyricTool;
    private LyricContentFrame mLyricContentFrame;

    /**
     * Creates new form ConvertAssToLrc
     */
    public ConvertAssToLrc() {
        initComponents();
        centerFrame();
        setTitle("ConvertAssToLrc");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        initArgument();
    }

    private void initArgument() {
        mLyricContentFrame = new LyricContentFrame();
        btnProcess.setEnabled(false);
        files = new ArrayList<File>();
        model = new DefaultListModel<>();
        modelProcess = new DefaultListModel<>();
        lvListChoose.setModel(model);
        lvProcessed.setModel(modelProcess);
        lvProcessed.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    System.out.println("index: " + index);
                    mLyricContentFrame.setDataResource(modelProcess.get(index).replace(LyricTool.CONTENT_TAG, ""));
                }
            }
        });
        mLyricTool = new LyricTool();
    }

    private ListSelectionListener onListSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }
    };

    private void centerFrame() {
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        setLocation(dx, dy);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lvListChoose = new javax.swing.JList<>();
        tvBrowser = new javax.swing.JLabel();
        btnBrowser = new javax.swing.JButton();
        btnProcess = new javax.swing.JButton();
        prgStatus = new javax.swing.JProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        lvProcessed = new javax.swing.JList<>();
        tvStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(lvListChoose);

        tvBrowser.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnBrowser.setText("Duyệt");
        btnBrowser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBrowserMouseClicked(evt);
            }
        });

        btnProcess.setText("Xử lý");
        btnProcess.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProcessMouseClicked(evt);
            }
        });

        jScrollPane2.setViewportView(lvProcessed);

        tvStatus.setText("0/1000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(tvBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowser, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProcess, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                    .addComponent(prgStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tvStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tvBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBrowser, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(btnProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prgStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tvStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowserMouseClicked
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\shoot\\Documents\\SkypeFilesReceiver\\LYRICS CHUẨN\\");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        FileFilter audioFilter = new FileNameExtensionFilter("Lyric files", "ass");
        fileChooser.setFileFilter(audioFilter);
        fileChooser.showOpenDialog(getMostRecentFocusOwner());
        if (fileChooser.getSelectedFiles().length == 0) {
            return;
        }
        System.out.println("Choosed");
        files.clear();
        model.clear();
        files.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
        for (File file : files) {
            tvBrowser.setText(file.getParent());
            model.addElement(file.getName());
        }

        tvStatus.setText(0 + "/" + files.size());
        prgStatus.setMaximum(files.size());
        prgStatus.setValue(0);
        btnProcess.setEnabled(true);
    }//GEN-LAST:event_btnBrowserMouseClicked

    private int success, fail;

    private void btnProcessMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProcessMouseClicked
        // TODO add your handling code here:
        btnBrowser.setEnabled(false);
        btnProcess.setEnabled(false);
        modelProcess.clear();
        tvStatus.setText(0 + "/" + files.size());
        success = fail = 0;
        mLyricTool.processInputFile(files, listener);
    }//GEN-LAST:event_btnProcessMouseClicked

    private LyricTool.OnLyricListener listener = new LyricTool.OnLyricListener() {
        @Override
        public void onSuccess(String lyric) {
            modelProcess.addElement(lyric);
            prgStatus.setValue(modelProcess.size());
            tvStatus.setText(modelProcess.size() + "/" + files.size());
            success++;
            if (modelProcess.size() == files.size()) {
                tvStatus.setText("Thành công: " + success + ", Thất bại: " + fail);
                btnBrowser.setEnabled(true);
            }
        }

        @Override
        public void onFail(String msg) {
            System.err.println(msg);
            modelProcess.addElement(msg);
            prgStatus.setValue(modelProcess.size());
            tvStatus.setText(modelProcess.size() + "/" + files.size());
            fail++;
            if (modelProcess.size() == files.size()) {
                tvStatus.setText("Thành công: " + success + ", Thất bại: " + fail);
                btnBrowser.setEnabled(true);
            }
        }
    };

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowser;
    private javax.swing.JButton btnProcess;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lvListChoose;
    private javax.swing.JList<String> lvProcessed;
    private javax.swing.JProgressBar prgStatus;
    private javax.swing.JLabel tvBrowser;
    private javax.swing.JLabel tvStatus;
    // End of variables declaration//GEN-END:variables
}
