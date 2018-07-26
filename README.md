# MIT - MIcroservice Trade-offs

**Data getting final causality consistent guarantee on eventually consistent data store**

This is a 


#### Notification service
Stores users contact information and notification settings (like remind and backup frequency). Scheduled worker collects required information from other services and sends e-mail messages to subscribed customers.


#### Geral Instructions:
- create a aws account (it's possible to have a free account)
- create a  bucket a configure a Cross-Region Replication (CRR)
- configure source and destination buckets, (this must have versioning enabled). For more information about versioning, see 
`curl -H "Using S3 versioning" http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html#_push_notifications_and_spring_cloud_bus`
- create a credentials(access_key_id and secret_access_key)
- install  Apache Maven
- install java 8 Release (JDK™)
```yml
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
- run on terminal ```mvn compile and package``` ServerFrame
- run on terminal ```ServerFrame.java``` Class 
- run on terminal ```ClientWriter.java``` Class 
- run on terminal ```ClientReader.java``` Class 

##### Notes
- choose different ports for communication between the microservice. (e.g. `9999` to `Writer C1` and `2222` to `Reader C2`).
- disable the firewall if the microservice is in different region or different machines
- There are significant [security notes](https://github.com/sqshq/PiggyMetrics#security) below



## Feedback welcome

PiggyMetrics is open source, and would greatly appreciate your help. Feel free to contact me with any questions.

`
Centre for Informatics and Systems of the University of Coimbra (CISUC), Portugal and Service Prototyping Lab, Zurich University of Applied Sciences, Winterthur, 8400 Switzerland
Copyright (C) 2017, 2018 Stanley Lima <stanleylima@dei.uc.pt>`
========================================================================

## Acknowledgment
This work was supported by research grants of the programs: Science Without Borders (Ciências sem Fronteiras - CsF), Brazilian Space Agency (Agência Espacial Brasileira - AEB).
