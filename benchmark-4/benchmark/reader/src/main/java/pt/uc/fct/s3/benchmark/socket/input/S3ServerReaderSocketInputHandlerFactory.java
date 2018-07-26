package pt.uc.fct.s3.benchmark.socket.input;

import pt.uc.fct.s3.benchmark.amazon.BucketAndPost;
import pt.uc.fct.s3.benchmark.frame.ClientReader;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketOutputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.api.SocketInputHandler;
import pt.uc.fct.s3.benchmark.socket.utils.command.CommandConstants;
import pt.uc.fct.s3.benchmark.socket.utils.command.ReaderWriterCommand;
import pt.uc.fct.s3.benchmark.socket.utils.command.ServerReaderCommand;
import pt.uc.fct.s3.benchmark.socket.utils.time.TimeManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class S3ServerReaderSocketInputHandlerFactory implements S3SocketInputHandlerFactory {
    private ClientReader clientReader;
    static FileWriter fw = null;
int i = 0;
    public S3ServerReaderSocketInputHandlerFactory(ClientReader clientReader) {
        this.clientReader = clientReader;
    }

    @Override
    public SocketInputHandler handler(BufferedReader reader, S3SocketOutputHandler outputHandler) {
        return new Handler(outputHandler, clientReader, reader);
    }

    public class Handler extends SocketInputHandler {
        private ClientReader clientReader;
        private BufferedReader reader;

        Handler(S3SocketOutputHandler outputHandler, ClientReader clientReader, BufferedReader reader) {
            super(outputHandler, clientReader);
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
                        ServerReaderCommand incomingCommand = ServerReaderCommand.valueOf(data[2]);

                        switch (incomingCommand) {
                            case CONSISTENT:
 /*
                                String uuid = data[0];
                                System.out.println("ID number of the connection to the server: " + uuid);
                                // open your connection
// TODO Stanley: 5/22/2018  Step 7 response time
                                String returnBucketPostClass = BucketAndPost.createBucketAndPost();//ler o dado consistent na Amazon
                                System.out.println("S3 Get result | TEXT FILE PARSING: " + returnBucketPostClass);
// TODO Stanley: 5/23/2018  Step 7 response time
                                //endTime = System.currentTimeMillis();
                                //System.out.println("#########Round trip response time = " + (endTime-startTime) + " milliseconds" + "\n");
                                UUID correlationId = UUID.fromString(data[1]);

                                long rtt = TimeManager.getInstance().rtt(correlationId);
                                double Throughtput = TimeManager.getInstance().throughput();



                                System.out.println("#########Round trip response time = " + (rtt) + " nanoseconds" + "\n");
                                //System.out.println("#########Accumulated Throughtput = " + TimeManager.getInstance().throughput() + "\n");
                                System.out.println("######### Throughtput = " + Throughtput + "\n");



                                File outPut = new File("outPut.txt");
                                    if (!outPut.exists()) {
                                        outPut.createNewFile();
                                    }
                                fw = new FileWriter(outPut, true);
                                    i=i+1;

                                fw.write("---Request:" + i +"\n" + "Throughtput:" + Throughtput + "\n" + "Round trip response time:" + (rtt) +  "\n");
                                fw.close();

                                // Throughput = number of requests / time to complete the requests
                                //Busy Time:
                                // Comple Transactions:
                                // Capacity:
                                //https://technet.microsoft.com/en-us/library/cc181325.aspx
                                //-------------------------------

                                // 1. Client connects to Server
                                // 2. Client sends data to server.
                                //3. Server Receives the client data
                                // 4. Server processes the data
                                // 5. Server sends data to Client
                                //6. Client received the response.

                                // Time from 1 -> 6 -- round trip time
                                // time 2 -> 3 & 5->6 -- network latency
                                // time 3->5 -- Response time for Server
                                //**time 2->6 -- Response Time for client  (Response time for Server + latency)
                                clientReader.invocaComandoWriter(ReaderWriterCommand.VALUE_READ, CommandConstants.EMPTY);
*/
                                break;
                            case DISCONNECT:
                                // TODO Stanley: 5/31/2018  
                                break;
                            case ACCEPTED:
                                UUID idDaLigacao = UUID.fromString(data[0]);
                                this.registerSocketConnection(idDaLigacao);
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
