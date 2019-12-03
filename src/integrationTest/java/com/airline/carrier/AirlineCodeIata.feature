
Feature: IATA Airline code lookup

Background:
* url 'http://localhost:8110/airline'
# * url config.baseUrl
* configure report = { showLog: true, showAllSteps: true }


Scenario: Retrieve a list of IATA airline codes
# Given url http://localhost:8110/airline/iata
Given path '/iata'
When method GET
Then status 200
And match $ contains { iataAirlineCode: 'AF' }


# ----- (GET) Lookup existing airline -----
Scenario Outline: Spot check a few IATA airline codes
Given path '/iata/<id>'
When method GET
Then status 200
And match $.iataAirlineCode == "<id>"
Examples:
| id |
| AF |
| KL |

# ----- (GET) lookup non-existent codes -----
Scenario Outline: Ensure unknown IATA airline codes are not found
Given path '/iata/<id>'
When method GET
Then status 404
Examples:
| id |
| SS |
| ZZ |



# ----- Add airline code (POST) -----
Scenario Outline: Add a new IATA airline code
Given path '/iata'
And request {iataAirlineCode: "<id>" }
When method POST
Then status 201

# When method GET
# Then status 200
# And match $.iata_code == <id>
# And match $ == {"iataAirlineCode":"<id>"}
And match $.iataAirlineCode == "<id>"
Examples:
| id |
| VS |
| 9V |



# ----- Put (Update) -----
# Scenario: Put (Update) an IATA code
# Given path '/iata/ATL'
# # When method GET
# # Then status 200

# And request {iataAirlineCode: "ATL" }
# When method PUT
# Then status 200

# ----- Put (Create) -----
# Scenario: Put an IATA code that does not yet exist
# Given path '/iata/SOS'
# When method GET
# Then status 404

# And request {iataAirlineCode: "SOS" }
# When method PUT
# Then status 201

# Given path '/iata/SOS'
# When method DELETE
# Then status 204



# ----- Delete -----
Scenario: Add, then delete an IATA airline code
Given path '/iata'
And request {iataAirlineCode: "SS" }
When method POST
Then status 201
    # now delete it - this is the real test
Given path '/iata/SS'
When method DELETE
Then status 204
