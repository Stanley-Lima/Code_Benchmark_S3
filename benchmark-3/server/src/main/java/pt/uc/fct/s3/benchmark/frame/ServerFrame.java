package pt.uc.fct.s3.benchmark.frame;


import pt.uc.fct.s3.benchmark.socket.S3ReaderServerSocketInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketManager;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.impl.S3SocketManagerImpl;
import pt.uc.fct.s3.benchmark.socket.utils.io.JtextAreaWriter;
import pt.uc.fct.s3.benchmark.socket.utils.time.TimeManager;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ServerFrame extends javax.swing.JFrame {
    private S3SocketManager socketManager;
    private Map<UUID, S3SocketOutputHandler> servingConnections;
    private List<String> users;

    public ServerFrame() {
        socketManager = new S3SocketManagerImpl();
        servingConnections = new HashMap<>();
        initComponents();
    }


    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        JtextAreaWriter.setArea(ta_chat);
        open_connection = new javax.swing.JButton();
        clear_window = new javax.swing.JButton();
        lb_name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("S3 benchmarking - Server");
        setName("server"); // NOI18N
        setResizable(false);

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);
        ta_chat.setForeground(Color.BLUE);

        open_connection.setText("Open socket  connection");
        open_connection.addActionListener(this::b_startActionPerformed);





        clear_window.setText("Clean Window log");
        clear_window.addActionListener(this::b_clearActionPerformed);

        lb_name.setText("FCTUC | Obeying causality - S3 benchmarking");
        lb_name.setBackground(Color.blue);
        lb_name.setFont(new Font("Serif", Font.BOLD, 18));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(open_connection, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                                        .addComponent(clear_window, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false))))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_name)
                                .addGap(209, 209, 209))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(open_connection))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(clear_window))
                                .addGap(18, 18, 18)
                                .addComponent(lb_name))
        );

        pack();
    }

    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        JtextAreaWriter.append("Server stopping... \n");

        JtextAreaWriter.setText("");
    }

    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {
        socketManager.serve(2222, new S3ReaderServerSocketInputHandlerFactory(this),servingConnections);
        JtextAreaWriter.append("Server started...\n");
    }

    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {
        JtextAreaWriter.append("\n Online users : \n");
        for (String current_user : users) {
            JtextAreaWriter.append(current_user);
            JtextAreaWriter.append("\n");
        }

    }

    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {
        JtextAreaWriter.setText("");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ServerFrame().setVisible(true));
    }

    public void invocaComando(final UUID connectionUuid, final Enum outgoingCommand, final String payload) {
        invocaComando(connectionUuid, outgoingCommand, payload, null);
    }

    public void invocaComando(final UUID connectionUuid, final Enum outgoingCommand, final String payload, UUID correlationUuid) {
        if (correlationUuid == null) {
            correlationUuid = UUID.randomUUID();
            TimeManager.getInstance().start(correlationUuid);
        }
        servingConnections.get(connectionUuid)
                .writeLn(CommandConstants.createCommandString(connectionUuid, correlationUuid, outgoingCommand, payload));
    }


    private javax.swing.JButton clear_window;
    private javax.swing.JButton open_connection;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_name;
    private javax.swing.JTextArea ta_chat;

}
