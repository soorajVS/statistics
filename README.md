# statistics


The API is to
calculate realtime statistic from the last 60 seconds. There will be two APIs, one of them is
called every time a transaction is made. It is also the sole input of this rest API. The other one
returns the statistic based of the transactions of the last 60 seconds.

API Endpoints :

POST /transactions

Every Time a new transaction happened, this endpoint will be called.
Body:
{
"amount": 12.3,
"timestamp": 1478192204000
}

Where:
● amount - transaction amount
● timestamp - transaction time in epoch in millis in UTC time zone (this is not current
timestamp)
Returns: Empty body with either 201 or 204.
● 201 - in case of success
● 204 - if transaction is older than 60 seconds
Where:
● amount is a double specifying the amount
● time is a long specifying unix time format in milliseconds


GET​ ​/statistics
It returns the statistic based on the transactions which happened in the last 60
seconds.

Where:
● sum is a double specifying the total sum of transaction value in the last 60 seconds
● avg is a double specifying the average amount of transaction value in the last 60
seconds
● max is a double specifying single highest transaction value in the last 60 seconds
● min is a double specifying single lowest transaction value in the last 60 seconds
● count is a long specifying the total number of transactions happened in the last 60
seconds


TO BUILD:

mvn clean package 
java -jar target/statistics-0.0.1-SNAPSHOT.jar


DESIGN :

   Controllers : StatisticController and TransactionController
   StatisticRepository has a AtomicReferenceArray containing immutable Statistic objects for each second.
   Same second transactions are merged to one Statistic Object. If it exceeds more than a minute , the reference will not be considered for aggregate statistics.
