package pt.uc.fct.s3.benchmark.socket.input;

import pt.uc.fct.s3.benchmark.frame.ClientWriter;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.api.SocketInputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.command.ReaderWriterCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

public class S3ReaderWriterSocketInputHandlerFactory implements S3SocketInputHandlerFactory {
    private ClientWriter clientWriter;

    public S3ReaderWriterSocketInputHandlerFactory(ClientWriter clientWriter) {
        this.clientWriter = clientWriter;
    }

    @Override
    public SocketInputHandler handler(BufferedReader reader, S3SocketOutputHandler outputHandler) {
        return new Handler(outputHandler, clientWriter, reader);
    }


    private class Handler extends SocketInputHandler {
        private ClientWriter clientWriter;
        private BufferedReader reader;

        Handler(S3SocketOutputHandler outputHandler, ClientWriter clientWriter, BufferedReader reader) {
            super(outputHandler, clientWriter);
            this.clientWriter = clientWriter;
            this.reader = reader;
        }

        @Override
        public void run() {
            String[] data;
            // String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";
//            UUID uuid = Generators.timeBasedGenerator().generate();
            String stream; //+ uuid.toString()
            //

            try {
                while ((stream = this.reader.readLine()) != null) {
                    try {
                        data = stream.split(CommandConstants.SPLIT_TOKEN);
                        ReaderWriterCommand incomingCommand = ReaderWriterCommand.valueOf(data[2]);

                        switch (incomingCommand) {
                            case VALUE_READ:
                                clientWriter.valueRead();
// TODO Stanley: 6/1/2018 Step in the interface
                                clientWriter.continueSending();
                                break;
                            case CONNECT:
                                break;
                            case DISCONNECT:
                                break;
                            case ACCEPTED:
                                UUID idDaLigacao = UUID.fromString(data[0]);
                                this.registerSocketConnection(idDaLigacao);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                //TODO nao conseguimos ler do socket, lidar com isso da melhor maneira. Reconect?
                e.printStackTrace();
            }
        }
    }
}
