# Backend Code Challenge

## Battery-REST-API-Spring-Boot
* This is a REST API developed using "spring boot" framework in JAVA language.
* For interaction with the Database, Spring Data JPA is used.
* Database - MYSQL
* Caching is enabled - Recent get requests communicates with data in cache memory avoiding repeated interaction with database

### Endpoints :- 

| Route                                                                 | Method | Parameters | Body                                                                                                                                                                                                           | Description                                                                                                                                                                                                  |
|-----------------------------------------------------------------------| ------ |----------- |----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| /battery/add-info                                                     | POST | - | ```[{"name":"batt1", "postcode": 444001, "wattCapacity": 333.45}, {...}, {...}, ..]```                                                                                                                         | This API endpoint expects list of batteries and adds them to the database.                                                                                                                                   |
| /battery/get-info?postcodeLowVal=int_value1&postcodeLowVal=int_value2 | GET | postcodeLowVal, postcodeHighVal | - | This API endpoint asks for batteries within supplied postcode range (postcodeLowVal - from postcode and postcodeHighVal - to postcode) along with total and average watt capacities of the fetched batteries |

### Authentication :-
As per the challenge, the above two mentioned endpoints can be accessed by anyone who runs this Spring Boot server, i.e., 
they do not require any authentication.

These endpoints can be made secure by having authentication prior to accessing the mention endpoints.
For authentication, there will be extra endpoints (for logging in, signing up, logging out).

Also, JWT token has to be used, which be acting as a middleware for accessing the mentioned two endpoints.
Once a user is logged in a new JWT token generated and gets stored in the header.
On each request, this token will passed in header to access the endpoints.
If and only if, there is token in the header, the user will be able to access the mentioned two battery endpoints.




