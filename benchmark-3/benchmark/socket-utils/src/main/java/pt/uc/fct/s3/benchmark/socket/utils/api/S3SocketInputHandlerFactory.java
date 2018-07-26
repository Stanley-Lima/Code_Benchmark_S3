package pt.uc.fct.s3.benchmark.socket.utils.api;

import java.io.BufferedReader;

public interface S3SocketInputHandlerFactory {
    SocketInputHandler handler(BufferedReader reader, S3SocketOutputHandler outputHandler);
}
