package pt.uc.fct.s3.benchmark.socket.utils.impl.output;

import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;

import java.io.PrintWriter;

public class S3SimpleSocketOutputHandler implements S3SocketOutputHandler {
    private PrintWriter writer;

    public S3SimpleSocketOutputHandler(PrintWriter writer) {
        this.writer = writer;
    }

    public void writeLn(String message) {
        this.writer.println(message);
        this.writer.flush();
    }
}
