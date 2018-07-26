package pt.uc.fct.s3.benchmark.frame;

import pt.uc.fct.s3.benchmark.amazon.Worker;
import pt.uc.fct.s3.benchmark.socket.input.S3ReaderWriterSocketInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketManager;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.api.SocketConnectionRegister;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.command.ReaderWriterCommand;
import pt.uc.fct.s3.benchmark.socket.utils.command.WriterReaderCommand;
import pt.uc.fct.s3.benchmark.socket.utils.impl.S3SocketManagerImpl;
import pt.uc.fct.s3.benchmark.socket.utils.io.JtextAreaWriter;
import pt.uc.fct.s3.benchmark.socket.utils.time.TimeManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;


public class ClientWriter extends javax.swing.JFrame implements SocketConnectionRegister {


    /**
     * InetAddress iAddress;
     * <p>
     * {
     * try {
     * iAddress = InetAddress.getLocalHost();
     * } catch (UnknownHostException e) {
     * e.printStackTrace();
     * }
     * }
     * <p>
     * String ip = iAddress.getHostAddress();
     **/
//private String username, address = "18.228.42.32"; tf_address

    private String username, address;

    // private String username, address = "localhost";
    private ArrayList<String> users = new ArrayList<>();
    private Boolean isConnected = false;

    private Map<UUID, S3SocketOutputHandler> connectionUUIDs;
    private List<S3SocketOutputHandler> servingConnections;
    private S3SocketManager socketManager;

    /**
     * Writer specific stuff
     */

    /**
     * Número de escritas pendentes. Clicar no enviar acrescenta a esta variável
     */
    int pendingWrites = 0;

    /**
     * Denota se o writer está à espera de uma confirmação de leitura
     */
    boolean awaitingValueRead = false;


    public ClientWriter() {
        connectionUUIDs = new HashMap<>();
        socketManager = new S3SocketManagerImpl();
        servingConnections = new ArrayList<>();
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


    private void initComponents() {

        lb_IP = new javax.swing.JLabel();
        tf_address = new javax.swing.JTextField();
        lb_port = new javax.swing.JLabel();
        tf_port = new javax.swing.JTextField();
        lb_username = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        b_connect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        JtextAreaWriter.setArea(ta_chat);
        tf_chat = new javax.swing.JTextField();
        b_send = new javax.swing.JButton();
        lb_name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("S3 benchmarking - Writer");
        setName("client"); // NOI18N
        setResizable(false);

        lb_IP.setText("IP");

        //tf_address.setText("18.228.42.32");
        //   tf_address.setText(ip.toString());
        tf_address.addActionListener(this::tf_addressActionPerformed);

        lb_port.setText("Port :");

        tf_port.setText("9999");
        tf_port.addActionListener(this::tf_portActionPerformed);

        lb_username.setText("Writer :");

        tf_username.addActionListener(this::tf_usernameActionPerformed);


        b_connect.setText("Connect Client Reader's");
        b_connect.addActionListener(this::b_connectActionPerformed);


        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);
        ta_chat.setForeground(Color.BLUE);

        b_send.setText("Request number");
        b_send.addActionListener(this::b_sendActionPerformed);


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
                                                .addComponent(tf_chat, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(lb_username, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                                        .addComponent(lb_IP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(tf_address, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                                        .addComponent(tf_username))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lb_port, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(tf_port, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(b_connect)
                                                                .addGap(2, 2, 2)
                                                                .addGap(0, 0, Short.MAX_VALUE)))))
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
                                        .addComponent(lb_IP)
                                        .addComponent(tf_address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lb_port)
                                        .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tf_username)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lb_username)
                                                .addComponent(b_connect)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tf_chat)
                                        .addComponent(b_send, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_name))
        );

        pack();
    }

    private void tf_addressActionPerformed(ActionEvent evt) {

    }

    private void tf_portActionPerformed(ActionEvent evt) {

    }

    private void tf_usernameActionPerformed(ActionEvent evt) {

    }

    private void b_connectActionPerformed(ActionEvent evt) {
        if (isConnected) {
            JtextAreaWriter.append("You are already connected. \n");
            return;
        }

        username = tf_username.getText();
        tf_username.setEditable(false);

        try {
            socketManager.connect(String.valueOf(tf_address.getText()), Integer.valueOf(tf_port.getText()), new S3ReaderWriterSocketInputHandlerFactory(this));
            isConnected = true;
            JtextAreaWriter.append("Connected. \n");
        } catch (Exception ex) {
            JtextAreaWriter.append("Cannot Connect! Try Again. \n");
            tf_username.setEditable(true);
            ex.printStackTrace();
        }

    }

    private void b_disconnectActionPerformed(ActionEvent evt) {
        for (UUID uuid : connectionUUIDs.keySet()) {
            sendDisconnect(uuid);
            disconnect(uuid);
        }
    }

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            pendingWrites += Integer.valueOf(tf_chat.getText());
            continueSending();
        } catch (NumberFormatException e) {
            JtextAreaWriter.append("Insert a number, please \n");
            JtextAreaWriter.append("Message was not sent. \n");
        } catch (Exception ex) {
            JtextAreaWriter.append("Message was not sent. \n");
        }
        tf_chat.setText(CommandConstants.EMPTY);
        tf_chat.requestFocus();


        tf_chat.setText(CommandConstants.EMPTY);
        tf_chat.requestFocus();
    }


    public void continueSending() throws Exception {
        if (!awaitingValueRead && pendingWrites > 0) {

// TODO Stanley: 6/1/2018 C1 Write
            String id = Worker.createBucketAndPost();

            // TODO Stanley: 5/31/2018 escolher um cliente qualquer. Trocar isto para escolher o pretendido se existirem mais do que um
            awaitingValueRead = true;
            for (UUID uuid : connectionUUIDs.keySet()) {
                invocaComando(uuid, WriterReaderCommand.WRITE_COMPLETE, id);
            }
            pendingWrites--;
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ClientWriter().setVisible(true));
    }


    private javax.swing.JButton b_connect;
    private javax.swing.JButton b_send;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_IP;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_port;
    private javax.swing.JLabel lb_username;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_address;
    private javax.swing.JTextField tf_chat;
    private javax.swing.JTextField tf_port;
    private javax.swing.JTextField tf_username;


    @Override
    public void registerSocketConnection(UUID uuid, S3SocketOutputHandler outputHandler) {
        connectionUUIDs.put(uuid, outputHandler);
    }

    public void valueRead() {
        awaitingValueRead = false;
    }
}
