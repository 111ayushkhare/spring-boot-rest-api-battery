# Backend Code Challenge

## Battery-REST-API-Spring-Boot
* This is a REST API developed using "spring boot" framework in JAVA language.
* For interaction with the Database, Spring Data JPA is used.
* Database - MYSQL
* Caching is enabled - Recent get requests communicates with data in cache memory avoiding repeated interaction with database

### Endpoints :- 
* /battery/add-info
  * Request type - POST
  * Request body - JSON data
  * This end point expects list of batteries (with name, postcode & watt_capacity of each)
* /battery/get-info
  * Request type - GET
  * Requires parameters - 
    * postcodeLowVal - from postcode
    * postcodeHighVal - to postcode
  * final_url - /battery/get-info?postcodeLowVal=2233&postcodeLowVal=2239

### Authentication :-
As per the challenge, the above two mentioned endpoints can be accessed by anyone who runs this Spring Boot server, i.e., 
they do not require any authentication.

These endpoints can be made secure by having authentication prior to accessing the mention endpoints.
For authentication, there will be extra endpoints (for logging in, signing up, logging out).

Also, JWT token has to be used, which be acting as a middleware for accessing the mentioned two endpoints.
Once a user is logged in a new JWT token generated and gets stored in the header.
On each request, this token will passed in header to access the endpoints.
If and only if, there is token in the header, the user will be able to access the mentioned two battery endpoints.




