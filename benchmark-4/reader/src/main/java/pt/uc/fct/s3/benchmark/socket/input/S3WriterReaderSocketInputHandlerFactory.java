package pt.uc.fct.s3.benchmark.socket.input;

import pt.uc.fct.s3.benchmark.amazon.BucketAndPost;
import pt.uc.fct.s3.benchmark.amazon.ConsistencyVerification;
import pt.uc.fct.s3.benchmark.frame.ClientReader;
import pt.uc.fct.s3.benchmark.socket.utils.api.S3SocketServerInputHandlerFactory;
import pt.uc.fct.s3.benchmark.socket.utils.command.*;
import pt.uc.fct.s3.benchmark.socket.utils.io.JtextAreaWriter;
import pt.uc.fct.s3.benchmark.socket.utils.time.TimeManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class S3WriterReaderSocketInputHandlerFactory implements S3SocketServerInputHandlerFactory {
    private ClientReader clientReader;
    static FileWriter FileThroughtput = null;
    static FileWriter FileRTRT = null;
    static FileWriter fw = null;

    int i = 0;

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

            String stream; //+ uuid.toString()
            //

            try {
                while ((stream = this.reader.readLine()) != null) {
                    try {
                        data = stream.split(CommandConstants.SPLIT_TOKEN);
                        WriterReaderCommand incomingCommand = WriterReaderCommand.valueOf(data[2]);

                        switch (incomingCommand) {
                            case WRITE_COMPLETE:
// TODO Stanley: 6/25/2018  C1 made a Write
                                JtextAreaWriter.append("WRITE_COMPLETE \n");
                                String idDoDado = data[3];
                                UUID connectionUuid = UUID.fromString(data[0]);
                                UUID correlationUuid = UUID.fromString(data[1]);
 // TODO Stanley: 6/25/2018  Inicio contagem de tempos
                                TimeManager.getInstance().start(correlationUuid);

// TODO Stanley: 6/25/2018   C2 read a data
                                String returnBucketPostClass = BucketAndPost.createBucketAndPost();//ler o dado consistent na Amazon
                                System.out.println("**********S3 Get result | TEXT FILE PARSING: " + returnBucketPostClass);
                                JtextAreaWriter.append("READ_COMPLETE \n");

// TODO Stanley: 6/25/2018  Measure and persist times
                                UUID correlationId = UUID.fromString(data[1]);
                                long rtt = TimeManager.getInstance().rtt(correlationId);
                                double Throughtput = TimeManager.getInstance().throughput();

                                System.out.println("#########Round trip response time = " + (rtt) + " nanoseconds" + "\n");
                                //System.out.println("#########Accumulated Throughtput = " + TimeManager.getInstance().throughput() + "\n");
                                System.out.println("######### Throughtput = " + Throughtput + "\n");



                                File outPutThroughtput = new File("outPutThroughtput.txt");
                                File outPutRTRT = new File("outPutRTRT.txt");
                                File outPut = new File("outPut.txt");

                                if (!outPutThroughtput.exists() ) {outPutThroughtput.createNewFile();}
                                if (!outPutRTRT.exists() ) {outPutRTRT.createNewFile();}
                                if (!outPut.exists() ) {outPut.createNewFile();}
                                i=i+1;



                                FileThroughtput = new FileWriter(outPutThroughtput, true);
                                FileRTRT = new FileWriter(outPutRTRT, true);
                                fw = new FileWriter(outPut, true);

                                fw.write("%S3 Get result | TEXT FILE PARSING: " + returnBucketPostClass  + "  ---Request:  " + i +"\n" + "  Throughtput =  " + Throughtput + "\n" + "RTRT: " + (rtt) +  "  %Round trip response time " + "\n" + "\r\n");
                                fw.close();


                                FileThroughtput.write(Throughtput +  ";" + "\n");
                                FileThroughtput.close();

                                FileRTRT.write( (rtt) + ";" + "\r\n");
                                FileRTRT.close();


// TODO Stanley: 6/25/2018 Say to Client Reader has done
                                clientReader.invocaComandoWriter(ReaderWriterCommand.VALUE_READ, CommandConstants.EMPTY);
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
                                break;
                            case DISCONNECT:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
