# N26 statistics usage

This proejcts is using:
- gradle as build tool (wrapper included)
- Spring as application framework
- H2 as in-memory database

### Building the project

The project can be built using this command in the command line:
`./gradlew clean build`

The application can be started with the following command:
`./gradlew bootRun`

### Create new transaction

Request: 
```
curl -XPOST http://localhost:8080/transactions -H"Content-type: application/json" -d '
{
	"amount" : 11,
	"timestamp" : 1498319555045
}'
```

Response can be either 201 or 204 (in case if the transaction is older than 60 secs)

### Get statistics
Request: 
```
curl http://localhost:8080/statistics
```

Response:
```
{
    "sum": 24,
    "avg": 12,
    "max": 13,
    "min": 11,
    "count": 2
}
```