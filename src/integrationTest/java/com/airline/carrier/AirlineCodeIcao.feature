
Feature: ICAO Airport code lookup

Background:
* url 'http://localhost:8110/airline/icao'
# * url config.baseUrl
* configure report = { showLog: true, showAllSteps: true }


Scenario: Retrieve a list of ICAO airline codes
# Given url http://localhost:8110/airline/icao
Given path '/'
When method GET
Then status 200
And match $ contains { icaoAirlineCode: 'LUH' }


# ----- (GET) Lookup existing airline -----
Scenario Outline: Spot check a few ICAO airline codes
Given path '/<id>'
When method GET
Then status 200
And match $.icaoAirlineCode == "<id>"
Examples:
| id  |
| LUH |
| DAL |

# ----- (GET) lookup non-existent codes -----
Scenario Outline: Ensure unknown ICAO airline codes are not found
Given path '/<id>'
When method GET
Then status 404
Examples:
| id  |
| SOS |
| ZZZ |



# ----- Add airline code (POST) -----
Scenario Outline: Add a new ICAO airline code
Given path '/'
And request {icaoAirlineCode: "<id>" }
When method POST
Then status 201
And match $.icaoAirlineCode == "<id>"
Examples:
| id  |
| VIR |
| VEX |



# ----- Put (Update) -----

# ----- Put (Create) -----


# ----- Delete -----
Scenario: Add, then delete an ICAO airline code
Given path '/'
And request {icaoAirlineCode: "SOS" }
When method POST
Then status 201
    # now delete it - this is the real test
Given path '/SOS'
When method DELETE
Then status 204
