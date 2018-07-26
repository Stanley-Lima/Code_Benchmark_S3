package pt.uc.fct.s3.benchmark.frame;

import pt.uc.fct.s3.benchmark.socket.input.S3ServerReaderSocketInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.input.S3WriterReaderSocketInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketManager;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.api.SocketConnectionRegister;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.command.ReaderServerCommand;
import pt.uc.fct.s3.benchmark.socket.utils.command.ReaderWriterCommand;
import pt.uc.fct.s3.benchmark.socket.utils.impl.S3SocketManagerImpl;
import pt.uc.fct.s3.benchmark.socket.utils.io.JtextAreaWriter;
import pt.uc.fct.s3.benchmark.socket.utils.time.TimeManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.UUID;


import java.util.*;


public class ClientReader extends javax.swing.JFrame implements SocketConnectionRegister {
    private String username, address ;
    private ArrayList<String> users = new ArrayList<>();
    private Boolean isConnected = false;

    private Map<UUID, S3SocketOutputHandler> connectionUUIDs;
    private Map<UUID, S3SocketOutputHandler> servingConnections;
    private S3SocketManager socketManager;


    public ClientReader() {
        connectionUUIDs = new HashMap<>();
        socketManager = new S3SocketManagerImpl();
        servingConnections = new HashMap<>();
        initComponents();
    }


    //--------------------------//

    public void userAdd(String data) {
        users.add(data);
    }

    //--------------------------//

    public void userRemove(String data) {
        JtextAreaWriter.append(data + " is now offline.\n");
    }

    //--------------------------//

    public void writeUsers() {
        String[] tempList = new String[(users.size())];
        users.toArray(tempList);
        for (String token : tempList) {
            //users.append(token + "\n");
        }
    }

    //--------------------------//

    public void sendDisconnect(UUID uuid) {
        String bye = (username + ": :Disconnect");
        try {
            invocaComando(uuid, ReaderWriterCommand.DISCONNECT, bye);
        } catch (Exception e) {
            JtextAreaWriter.append("Could not send Disconnect message.\n");
        }
    }

    //--------------------------//

    public void disconnect(UUID uuid) {
        try {
            JtextAreaWriter.append("Disconnected.\n");
            socketManager.disconnect(uuid);
            connectionUUIDs.remove(uuid);
        } catch (Exception ex) {
            JtextAreaWriter.append("Failed to disconnect. \n");
        }
        isConnected = false;
        tf_username.setEditable(true);

    }

    public void invocaComandoServidor(ReaderServerCommand comando, String payload) {
        invocaComando(connectionUUIDs.keySet().iterator().next(), comando, payload);
    }

    public void invocaComandoWriter(ReaderWriterCommand comando, String payload) {
        invocaComandoServico(servingConnections.keySet().iterator().next(), comando, payload);

    }


    private void invocaComando(final UUID connectionUuid, final Enum outgoingCommand, final String payload) {
        invocaComando(connectionUuid, outgoingCommand, payload, null);
    }

    private void invocaComando(final UUID connectionUuid, final Enum outgoingCommand, final String payload, UUID correlationUuid) {
        if (correlationUuid == null) {
            correlationUuid = UUID.randomUUID();
            TimeManager.getInstance().start(correlationUuid);
        }
        connectionUUIDs.get(connectionUuid)
                .writeLn(CommandConstants.createCommandString(connectionUuid, correlationUuid, outgoingCommand, payload));

    }

    private void invocaComandoServico(final UUID connectionUuid, final Enum outgoingCommand, final String payload) {
        invocaComandoServico(connectionUuid, outgoingCommand, payload, null);

    }

    private void invocaComandoServico(final UUID connectionUuid, final Enum outgoingCommand, final String payload, UUID correlationUuid) {
        if (correlationUuid == null) {
            correlationUuid = UUID.randomUUID();
            TimeManager.getInstance().start(correlationUuid);
        }
        servingConnections.get(connectionUuid)
                .writeLn(CommandConstants.createCommandString(connectionUuid, correlationUuid, outgoingCommand, payload));

    }


    private void initComponents() {

        lb_address = new javax.swing.JLabel();
        TextField_ip = new javax.swing.JTextField();
        lb_port = new javax.swing.JLabel();
        bt_port = new javax.swing.JTextField();
        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        b_connect = new javax.swing.JButton();
        b_openSocketButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        JtextAreaWriter.setArea(ta_chat);
        lb_name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("S3 benchmarking - Reader");
        setName("client"); // NOI18N
        setResizable(false);

        lb_address.setText("IP");

     //   TextField_ip.setText("localhost");
        TextField_ip.addActionListener(this::tf_addressActionPerformed);

        lb_port.setText("Port :");

        bt_port.setText("2222");
        bt_port.addActionListener(this::tf_portActionPerformed);

        lb_username.setText("Reader :");

        tf_username.addActionListener(this::tf_usernameActionPerformed);


        b_connect.setText("Connect Client Reader's");
        b_connect.addActionListener(this::b_connectActionPerformed);



        b_openSocketButton.setText("Open Socket Connection");
        b_openSocketButton.addActionListener(this::b_openSocketConnection);

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);
        ta_chat.setForeground(Color.BLUE);




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
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(lb_username, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                                        .addComponent(lb_address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(TextField_ip, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                                        .addComponent(tf_username))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lb_port, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(bt_port, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(b_connect)
                                                                .addGap(2, 2, 2)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addComponent(b_openSocketButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_name)
                                .addGap(201, 201, 201))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lb_address)
                                        .addComponent(TextField_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lb_port)
                                        .addComponent(bt_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(b_openSocketButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tf_username)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lb_username)
                                                .addComponent(b_connect)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_name))
        );

        pack();
    }

    private void tf_addressActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void tf_portActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void tf_usernameActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void b_connectActionPerformed(java.awt.event.ActionEvent evt) {
        if (isConnected) {
            JtextAreaWriter.append("You are already connected. \n");
            return;
        }

        username = tf_username.getText();
        tf_username.setEditable(false);

        try {
            socketManager.connect(TextField_ip.getText(), Integer.valueOf(bt_port.getText()), new S3ServerReaderSocketInputHandlerFactory(this));
            isConnected = true;
            JtextAreaWriter.append("Connected. \n");
        } catch (Exception ex) {
            JtextAreaWriter.append("Cannot Connect! Try Again. \n");
            tf_username.setEditable(true);
            ex.printStackTrace();
        }

    }

    private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt) {
        for (UUID uuid : connectionUUIDs.keySet()) {
            sendDisconnect(uuid);
            disconnect(uuid);
        }
    }

    private void b_openSocketConnection(ActionEvent evt) {
        socketManager.serve(9999, new S3WriterReaderSocketInputHandlerFactory(this), servingConnections);
    }



    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ClientReader().setVisible(true));
    }

    private javax.swing.JButton b_openSocketButton;
    private javax.swing.JButton b_connect;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_address;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_port;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField TextField_ip;
    private javax.swing.JTextField bt_port;
    private javax.swing.JTextField tf_username;


    @Override
    public void registerSocketConnection(UUID uuid, S3SocketOutputHandler outputHandler) {
        connectionUUIDs.put(uuid, outputHandler);
    }
}
