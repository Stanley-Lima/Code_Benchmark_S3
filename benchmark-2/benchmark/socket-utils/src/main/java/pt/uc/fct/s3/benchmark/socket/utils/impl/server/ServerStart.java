package pt.uc.fct.s3.benchmark.socket.utils.impl.server;

import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketServerInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommonCommand;
import pt.uc.fct.s3.benchmark.socket.utils.impl.output.S3SimpleSocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.io.JtextAreaWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class ServerStart implements Runnable {
    private final Map<UUID, S3SocketOutputHandler> outputHandlers;
    private final S3SocketServerInputHandlerFactory inputHandlerFactory;
    private final int port;

    public ServerStart(int port, Map<UUID, S3SocketOutputHandler> outputHandlers, S3SocketServerInputHandlerFactory inputHandlerFactory) {
        this.port = port;
        this.outputHandlers = outputHandlers;
        this.inputHandlerFactory = inputHandlerFactory;
    }


    public void run() {
        try {
            ServerSocket serverSock = new ServerSocket(port);

            while (true) {
                Socket clientSock = serverSock.accept();
                //TODO checar melhor a lógica dos ids
                UUID connectionUUID = UUID.randomUUID();
                S3SimpleSocketOutputHandler outputHandler = new S3SimpleSocketOutputHandler(new PrintWriter(clientSock.getOutputStream()));
                outputHandlers.put(connectionUUID, outputHandler);
                outputHandler.writeLn(CommandConstants.createCommandString(connectionUUID, null, CommonCommand.ACCEPTED, CommandConstants.EMPTY));

                (new Thread(
                        inputHandlerFactory.handler(
                                new BufferedReader(
                                        new InputStreamReader(clientSock.getInputStream())))))
                        .start();
                JtextAreaWriter.append("Got a connection. \n");
            }
        } catch (Exception ex) {
            JtextAreaWriter.append("Error making a connection. \n");
        }
    }
}
