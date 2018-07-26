package pt.uc.fct.s3.benchmark.socket.utils.api;

import java.util.UUID;

public abstract class SocketInputHandler implements Runnable {
    private S3SocketOutputHandler outputHandler;
    private SocketConnectionRegister socketConnectionRegister;

    public SocketInputHandler(S3SocketOutputHandler outputHandler, SocketConnectionRegister socketConnectionRegister) {
        this.outputHandler = outputHandler;
        this.socketConnectionRegister = socketConnectionRegister;
    }

    protected void registerSocketConnection(UUID uuid){
        socketConnectionRegister.registerSocketConnection(uuid, outputHandler);
    }



}
