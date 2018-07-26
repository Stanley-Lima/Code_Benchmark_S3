# MIT - MIcroservice Trade-offs

**Data getting final causality consistent guarantee on eventually consistent data store**
![benchmark-1-page-3](https://user-images.githubusercontent.com/7977251/43281928-ccee4180-910c-11e8-9f59-85cd05a00e1a.png)

This experiment was developed under the PDCTI | DOCTORAL PLAN IN INFORMATION SCIENCES AND TECHNOLOGIES.
The different lemmas, experimental results and analysis of the  different strategies for implementing Causal order and different benchmarks for performance comparison of these strategies, were submitted to Journal of parallel and distributed computing.
See full article: `https://www.journals.elsevier.com/journal-of-parallel-and-distributed-computing/editorial-board` modify for direct link when paper is online.



#### Geral Instructions:
- create a aws account (it's possible to have a free account)
- create a  bucket a configure a Cross-Region Replication (CRR)
- configure source and destination buckets, (this must have versioning enabled). For more information about versioning, see 
`"Using S3 versioning" https://docs.aws.amazon.com/AmazonS3/latest/dev/crr.html`
- create a credentials(access_key_id and secret_access_key)
- install  Apache Maven
- install java 8 Release (JDK™)
```pom
Maven Dependency:
 <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.11</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>s3</artifactId>
                <version>2.0.0-preview-8</version>
            </dependency>
            <dependency>
                <groupId>com.fasterxml.uuid</groupId>
                <artifactId>java-uuid-generator</artifactId>
                <version>3.1.4</version>
            </dependency>
            <dependency>
                <groupId>com.amazonaws</groupId>
                <artifactId>aws-java-sdk</artifactId>
                <version>1.11.319</version>
            </dependency>
            <dependency>
```

#### Instructions to run:
- run on terminal ```mvn compile and package```

- ```start /b java -jar reader/target/reader-1.0-SNAPSHOT.jar```
- ```start /b java -jar server/target/server-1.0-SNAPSHOT.jar```
- ```start /b java -jar writer/target/writer-1.0-SNAPSHOT.jar```
- ```//java -jar reader/target/reader-1.0-SNAPSHOT.jar```
- ```//Server class S3ServerReaderSocketInputHandlerFactory, (ServerFrame, ClientWrite, ClientReader) or runClientWriterServer.bat```
If you have all configurations performed, you can use the "runAllMicrocervices.bat" file to run all microservices together.
##### Notes
- choose different ports for communication between the microservice. (e.g. `9999` to `Writer C1` and `2222` to `Reader C2`).
- disable the firewall if the microservice is in different region or different machines.
- the logs in the files, "logReader", "logWritter", "outPut", "outPutRTRT" and "outPutThroughtput" are results referring to the above paper.




## Feedback welcome

This code is Open-source, and would greatly appreciate your help. Feel free to contact me with any questions.

Centre for Informatics and Systems of the University of Coimbra (CISUC), Portugal and Service Prototyping Lab, Zurich University of Applied Sciences, Winterthur, 8400 Switzerland
Copyright (C) 2017, 2018 Stanley Lima <stanleylima@dei.uc.pt>

## Acknowledgment
This work was supported by research grants of the programs: Science Without Borders (Ciências sem Fronteiras - CsF), Brazilian Space Agency (Agência Espacial Brasileira - AEB).
