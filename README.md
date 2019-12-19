# The article - Right After Causal Consistency is Dependability for Cloud Storage Services: Throughput Lessons Learnt from Long-Term Experimental Research

Centre for Informatics and Systems of the University of Coimbra (CISUC), Portugal and Service Prototyping Lab, Zurich University of Applied Sciences, Winterthur, 8400 Switzerland

 2019 PhD student Stanley Lima <stanleylima@dei.uc.pt> Advisors: Filipe Araujo and Josef Spillner

This experiment was developed under the PDCTI | DOCTORAL PLAN ON: DEI | Informatics Engineering Department .

================================================
Abstract: For the sake of offering highly available, low latency services, cloud storage systems explicitly drop strong consistency requirements. This is unacceptable for applications requiring fresh data. That is, loosen the coupling in favor of reliability to achieve a stronger than eventual level of consistency. While the PACELC theorem explicitly prevents consistent storage from being fast, we can nevertheless explore the trade-off between consistency, availability, and partition tolerance in distributed storage systems in order to get fast access to data.
This paper describes our effort to achieve a good trade-off between performance and a causal consistency model.
We evaluated a set of algorithms running on Amazon EC2 capable of ensuring causal consistency to the S3 cross-region replication service (CRR) and compare them against a baseline eventual implementation.
Our results show that one can achieve quite reasonable results and still ensure the quite useful causal consistency model.



**Benchmarking -Scenario-1: Repeating read attempts from replica**

![benchmark-1-page-3](https://user-images.githubusercontent.com/7977251/43281928-ccee4180-910c-11e8-9f59-85cd05a00e1a.png)

Were submitted to Journal of xxxxxx. See full article: `https://www.journals.xxxxx` modify for direct link when paper is online.




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

- ```start /b java -jar reader/target/reader-1.0-SNAPSHOT```
- ```start /b java -jar server/target/server-1.0-SNAPSHOT```
- ```start /b java -jar writer/target/writer-1.0-SNAPSHOT```
- ```//java -jar reader/target/reader-1.0-SNAPSHOT```
- ```//Server class S3ServerReaderSocketInputHandlerFactory, (ServerFrame, ClientWrite, ClientReader) or runClientWriterServer.bat```
If you have all configurations performed, you can use the "runAllMicrocervices.bat" file to run all microservices together.
##### Notes
- choose different ports for communication between the microservice. (e.g. `9999` to `Writer C1` and `2222` to `Reader C2`).
- disable the firewall if the microservice is in different region or different machines.
- the logs in the files, "logReader", "logWritter", "outPut", "outPutRTRT" and "outPutThroughtput" are results referring to the above paper.




## Feedback welcome

Feel free to contact me with any questions.

## Acknowledgment
This work was supported by research grants of the programs: Science Without Borders (Ciências sem Fronteiras - CsF), Brazilian Space Agency (Agência Espacial Brasileira - AEB).

File structure:
---------------
![file structure](https://user-images.githubusercontent.com/7977251/43429685-96be26f4-945c-11e8-89d2-a42580b00dd1.jpg)
	
	
