# banking 
This is a simple REST service which caters to below use cases:
1. create customers
2. create accounts
3. withdraw, deposit or transfer money in accounts

Database is in-memomry No SQL db for the sake of simplicity.

Project is built using Maven and runs as a Dropwizard service.

To generate the Dropwizard artifact, execute below command:

mvn package

this will generate "target\banking-1.0-SNAPSHOT.jar"

To run the service execute below command:

java -jar target\banking-1.0-SNAPSHOT.jar server banking.yml

In case there is a conflict in port number, please update banking.yml with available port numbers

Basic integration tests for all positive use cases are written in IntegrationTest.groovy.

Few enhancements that could be added to the project:
1. ACID transactions
2. Logging
3. Custom codes and messages for validation failure
4. Swagger documentation
5. Batch job to convert Pending transactions into Committed transactions and update balance field in accounts so that on every get or process transaction request fewer PENDING transactions are pulled
6. Unit testing for validations
