package pt.uc.fct.s3.benchmark.socket.utils.api;

public interface S3SocketOutputHandler {

    /**
     * Writes the provided message to the socket followed by a line feed
     *
     * @param message the message to send through the socket
     */
    void writeLn(String message);

}
