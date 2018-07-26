package pt.uc.fct.s3.benchmark.socket.input;

import pt.uc.fct.s3.benchmark.frame.ClientReader;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketServerInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.command.ReaderServerCommand;
import pt.uc.fct.s3.benchmark.socket.utils.command.WriterReaderCommand;

import java.io.BufferedReader;
import java.io.IOException;

public class S3WriterReaderSocketInputHandlerFactory implements S3SocketServerInputHandlerFactory {
    private ClientReader clientReader;

    public S3WriterReaderSocketInputHandlerFactory(ClientReader clientReader) {
        this.clientReader = clientReader;
    }

    public Runnable handler(BufferedReader reader) {
        return new Handler(clientReader, reader);
    }

    public class Handler implements Runnable {

        private ClientReader clientReader;
        private BufferedReader reader;

        Handler(ClientReader clientReader, BufferedReader reader) {
            this.clientReader = clientReader;
            this.reader = reader;
        }

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
                        WriterReaderCommand incomingCommand = WriterReaderCommand.valueOf(data[2]);

                        switch (incomingCommand) {
                            case WRITE_COMPLETE:
                                String idDoDado = data[3];
// TODO Stanley: 6/1/2018 Step 2
                                clientReader.invocaComandoServidor(ReaderServerCommand.CONSISTENCY, idDoDado);
                                break;
                            case DISCONNECT:
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
