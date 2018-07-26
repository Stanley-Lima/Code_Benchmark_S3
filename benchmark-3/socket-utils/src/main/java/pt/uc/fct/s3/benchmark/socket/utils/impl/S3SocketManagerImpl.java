package pt.uc.fct.s3.benchmark.socket.utils.impl;

import javafx.util.Pair;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketManager;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketServerInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.impl.output.S3SimpleSocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.impl.server.ServerStart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class S3SocketManagerImpl implements S3SocketManager {
    private final Map<Integer, Thread> serverThreads;
    private final Map<UUID, Pair<Socket, Thread>> socketMap;

    public S3SocketManagerImpl() {
        serverThreads = new HashMap<>();
        socketMap = new HashMap<>();
    }

    public void serve(int port, S3SocketServerInputHandlerFactory inputHandlerFactory, Map<UUID, S3SocketOutputHandler> outputHandlers) {
        if (serverThreads.get(port) != null) {
            throw new RuntimeException("Already serving at that port!");
        }
        Thread thread = new Thread(new ServerStart(port, outputHandlers, inputHandlerFactory));
        serverThreads.put(port, thread);
        thread.start();
    }


    public void stopServe(int port) {
        if (serverThreads.get(port) == null) {
            throw new RuntimeException("Not serving at that port!");
        }
        serverThreads.get(port).interrupt();
        serverThreads.remove(port);
    }

    public void connect(String host, int port, S3SocketInputHandlerFactory inputHandlerFactory) throws IOException {
        Socket socket = new Socket(host, port);
        final Thread thread = new Thread(
                inputHandlerFactory.handler(
                        new BufferedReader(new InputStreamReader(socket.getInputStream())),
                        new S3SimpleSocketOutputHandler(new PrintWriter(socket.getOutputStream()))));
        thread.start();

    }

    public void disconnect(UUID uuid) throws IOException {
        Pair<Socket, Thread> socketThreadPair = socketMap.get(uuid);
        if (socketThreadPair == null) {
            throw new RuntimeException("That socket is not registered or is already disconnected!");
        }
        socketThreadPair.getValue().interrupt();
        socketThreadPair.getKey().close();
        socketMap.remove(uuid);

    }

}
