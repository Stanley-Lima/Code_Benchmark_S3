package pt.uc.fct.s3.benchmark.socket.utils.api;

import java.util.UUID;

public interface SocketConnectionRegister {
    void registerSocketConnection(UUID uuid, S3SocketOutputHandler outputHandler);
}
