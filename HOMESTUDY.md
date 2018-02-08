## Documentation

1. Object Oriented Analysis & Design Tutorial https://www.tutorialspoint
.com/object_oriented_analysis_design ~ 30 minutes
2. Spring Container and Beans https://docs.spring ~ 3 hours
.io/spring/docs/current/spring-framework-reference/core.html#beans 
3. Hibernate 5 Getting started - https://docs.jboss.org/hibernate/orm/5.0/quickstart/html/ ~ 30 
minutes
4. Hibernate for beginners - https://www.journaldev.com/3793/hibernate-tutorial ~ 2 hours
5. Hibernate user guide - https://docs.jboss.org/hibernate/orm/5
.2/userguide/html_single/Hibernate_User_Guide.html#architecture ~ 3 hours
6. Hibernate integration https://docs.spring
.io/spring/docs/current/spring-framework-reference/data-access.html#orm-hibernate ~ 1 hour
7. Spring MVC https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc 
~ 2 hours
8. FreeMarker templates - http://freemarker.org/docs/index.html ~ 2 hours
9. FreeMarker integration - https://docs.spring
.io/spring/docs/current/spring-framework-reference/web.html#mvc-view-freemarker ~ 30 hours 
10. Spring - JPA https://docs.spring.io/spring-data/jpa/docs/current/reference/html/ ~ 1 hour

## Creation of AWS EC2 instance.

https://aws.amazon.com/getting-started/tutorials/launch-a-virtual-machine/?nc1=h_ls

## Configuration of Log4j (version 1.2 or optionally 2.0)

Application that will write must have logging with Apache Log4j.

1. log4j Tutorial - https://www.tutorialspoint.com/log4j/index.htm ~ 30 minutes
2. log4j Manual - https://logging.apache.org/log4j/1.2/manual.html ~ 30 minutes
3. log4j 2 manual - https://logging.apache.org/log4j/2.x/manual/api.html, 
       https://logging.apache.org/log4j/2.x/manual/configuration.html~ 1 hour


## Deployment of web application into Tomcat.

1. Download and unzip Apache Tomcat. Here  http://apache.volia.net is used as download mirror.
It may be changed. You can check localized link on download on official Apache Tomcat site
https://tomcat.apache.org/download-90.cgi
```
mkdir ~/external-services-example
cd ~/external-services-example
wget http://apache.ip-connect.vn.ua/tomcat/tomcat-9/v9.0.1/bin/apache-tomcat-9.0.1.tar.gz
tar -xvzf apache-tomcat-9.0.1.tar.gz
rm apache-tomcat-9.0.1.tar.gz
```
2. Build your web application. Copy resulting web archive file into _apache-tomcat-9.0.1/webapps_
directory.
```
cd ${PATH_TO_YOUR_PROJECT}/external-api-services
mvn clean install
mv ${PATH_TO_YOUR_PROJECT}/external-api-services/target/external-api-services.war ~/external-services-example/apache-tomcat-9.0.1/webapps/
~/external-services-example/apache-tomcat-9.0.1/webapps/
```
3. (Optionally) Install MySQL database (Debian APT) and check it is working.
```
sudo apt-get install mysql-server
mysql -u root -p
```
4. (Optionally) Execute create script for given server.
```
mysql -u root -p < create-script.sql
``` 
5. Start Tomcat server.
```
cd ~/external-services-example
./apache-tomcat-9.0.1/bin/startup.sh
```
6. Check web application is up and running. 
```
curl -v http://localhost:8080/external-api-services/api/airports/byCity?city=Kiev
```
## Monitoring EC2 with AWS CloudWatch

1. Collect Metrics - http://docs.aws.amazon
.com/AmazonCloudWatch/latest/monitoring/ec2-metricscollected
.html ~ 20 minutes
2. Additional Metrics - http://docs.aws.amazon
.com/AmazonCloudWatch/latest/monitoring/as-metricscollected.html ~ 20 minutes
3. Send CPU alert -  http://docs.aws.amazon
.com/AmazonCloudWatch/latest/monitoring/US_AlarmAtThresholdEC2
.html ~ 30 minutes
4. Send storage alert -
 http://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/US_AlarmAtThresholdEBS.html ~ 30 mins
