package pt.uc.fct.s3.benchmark.socket;

import pt.uc.fct.s3.benchmark.amazon.ConsistencyVerification;
import pt.uc.fct.s3.benchmark.frame.ServerFrame;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketServerInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.command.ReaderServerCommand;
import pt.uc.fct.s3.benchmark.socket.utils.command.ServerReaderCommand;
import pt.uc.fct.s3.benchmark.socket.utils.io.JtextAreaWriter;
import pt.uc.fct.s3.benchmark.socket.utils.time.TimeManager;

import java.io.BufferedReader;
import java.util.UUID;

public class S3ReaderServerSocketInputHandlerFactory implements S3SocketServerInputHandlerFactory {
    private ServerFrame serverFrame;
    public static String consistentReplica;


    public S3ReaderServerSocketInputHandlerFactory(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
    }

    @Override
    public Runnable handler(BufferedReader reader) {
        return new Handler(serverFrame, reader);
    }

    private class Handler implements Runnable {
        private ServerFrame serverFrame;
        private BufferedReader reader;

        Handler(ServerFrame serverFrame, BufferedReader reader) {
            this.reader = reader;
            this.serverFrame =serverFrame;
        }

        @Override
        public void run() {
            try {
                String message;
                String[] data;


                while ((message = reader.readLine()) != null) {
                    // TODO Stanley: 5/20/2018 validar formato da mensagem
                    data = message.split(CommandConstants.SPLIT_TOKEN);
                    ReaderServerCommand command = ReaderServerCommand.valueOf(data[2]);
                    switch (command){
                        case CONSISTENCY:
                            String idDoDado = data[3];
 // TODO Stanley: 6/1/2018 Step 4
                             consistentReplica =   ConsistencyVerification.readBucket(idDoDado);
                             //idDoDado = consistentReplica;

                            JtextAreaWriter.append("Consistency\n");
                            UUID connectionUuid = UUID.fromString(data[0]);
                            UUID correlationUuid = UUID.fromString(data[1]);
                            serverFrame.invocaComando(connectionUuid,
                                    ServerReaderCommand.CONSISTENT
                                    ,consistentReplica
                                    ,correlationUuid);
                            break;
                        case VALUE_READ:
                            break;
                        case DISCONNECT:
                            break;
                    }
                }
            } catch (Exception ex) {
                JtextAreaWriter.append("Lost a connection. \n");
                ex.printStackTrace();
            }
        }
    }
}
