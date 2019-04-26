@apicuritoTests
@dataTypesTests
Feature: Test everything on data types page

  Background:
    Given delete API "tmp/download/openapi-spec.json"
    And log into apicurito
    And create a new API
    And initialize collector

  @twoDataTypes
  Scenario: create 2 data types with/without rest
    When create a new data type with link
      | data1 | best data type ever | false | true  |
      | data2 | desc                | true  | false |

    And select data type "data1"
    And set data type description "best data type"
    And create data type property with name "MyProperty"
    And set description "My desc" for data type property "MyProperty"

    And set property type "Integer" for property "MyProperty"
    And set property type as "32-Bit Integer" for property "MyProperty"

    And set property "MyProperty" as "Required"

    And select data type "data2"
    And set data type example "MyExample"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    Then check that data type "data1" is created
    And select data type "data1"
    And check that data type description is "best data type"
    And check that data type property "MyProperty" is created

    And check that description is "My desc" for property "MyProperty"
    And check that type is "Integer" for property "MyProperty"
    And check that type as is "32-Bit Integer" for property "MyProperty"
    And check that property "MyProperty" is "Required"

    And check that data type "data2" is created
    And select data type "data2"
    And check that example is "MyExample"

    And check that path "/data2S" is created
    And check that operation "GET" is created for path "/data2S"
    When select path "/data2S"
    And select operation "GET"
    Then check that operation summary is "List All data2S"
    And check that operation ID is "getdata2S"
    And check that operation description is "Gets a list of all data2 entities."

    And check that exist response 200
    And check that description is "Successful response - returns an array of data2 entities." for response 200
    And check that type is "Array" for response 200
    And check that type of is "data2" for response 200

    And check that operation "POST" is created for path "/data2S"
    When select path "/data2S"
    And select operation "POST"
    Then check that operation summary is "Create a data2"
    And check that operation ID is "createdata2"
    And check that operation description is "Creates a new instance of a data2."

    And check that exist response 201
    And check that description is "Successful response." for response 201
    #Choose type mean that the type is not set
    And check that type is "Choose Type" for response 201

    And check that request body description is "A new data2 to be created."
    And check that request body type is "data2"

    And check that path "/data2S/{data2Id}" is created
    And check that operation "GET" is created for path "/data2S/{data2Id}"
    When select path "/data2S/{data2Id}"

    And check that path parameter "data2Id" is created for path "/data2S/{data2Id}"

    And select operation "GET"
    Then check that operation summary is "Get a data2"
    And check that operation ID is "getdata2"
    And check that operation description is "Gets the details of a single instance of a data2."

    And check that exist response 200
    And check that description is "Successful response - returns a single data2." for response 200
    And check that type is "data2" for response 200

    And check that operation "PUT" is created for path "/data2S/{data2Id}"
    When select path "/data2S/{data2Id}"
    And select operation "PUT"
    Then check that operation summary is "Update a data2"
    And check that operation ID is "updatedata2"
    And check that operation description is "Updates an existing data2."

    And check that exist response 202
    And check that description is "Successful response." for response 202
    #Choose type mean that the type is not set
    And check that type is "Choose Type" for response 202

    And check that request body description is "Updated data2 information."
    And check that request body type is "data2"

    And check that operation "DELETE" is created for path "/data2S/{data2Id}"
    When select path "/data2S/{data2Id}"
    And select operation "DELETE"
    Then check that operation summary is "Delete a data2"
    And check that operation ID is "deletedata2"
    And check that operation description is "Deletes an existing data2."

    And check that exist response 204
    And check that description is "Successful response." for response 204
    #Choose type mean that the type is not set
    And check that type is "Choose Type" for response 204

    Then check all for errors