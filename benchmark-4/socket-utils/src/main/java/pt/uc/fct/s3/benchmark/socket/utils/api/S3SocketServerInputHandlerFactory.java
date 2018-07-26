package pt.uc.fct.s3.benchmark.socket.utils.api;

import java.io.BufferedReader;

public interface S3SocketServerInputHandlerFactory {
    Runnable handler(BufferedReader reader);
}
