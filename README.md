# banking 
This is a simple REST service which caters to below use cases:
1. create customers
2. create accounts
3. withdraw, deposit or transfer money in accounts

Database is in-memomry No SQL db for the sake of simplicity.

Enhancements required:
1. ACID transactions
2. Logging
3. Custom codes and messages for validation failure
4. Swagger documentation
5. Batch job to convert Pending transactions into Committed transactions and update balance field in accounts
6. Unit testing for validations
