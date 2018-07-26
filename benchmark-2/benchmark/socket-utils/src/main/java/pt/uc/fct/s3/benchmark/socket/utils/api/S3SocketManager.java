package pt.uc.fct.s3.benchmark.socket.utils.api;

import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface S3SocketManager {
    void serve(int port, S3SocketServerInputHandlerFactory inputHandler, Map<UUID,S3SocketOutputHandler> outputHandlers);
    void stopServe(int port);
    void connect(String host, int port, S3SocketInputHandlerFactory inputHandler) throws IOException;
    void disconnect(UUID uuid) throws IOException;
}
