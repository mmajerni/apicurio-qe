@apicuritoTests
Feature: simpleTestMainPage
  Background:
      Given delete API "tmp/download/openapi-spec.json"
      And log into apicurito
      And create a new API
@basic
  Scenario: basic main page test
      And change API name to "MyApi"
      And set API version to "2.20a"
      And change description to "New API desc"
      And set consumes to "text/xml"
      And set produces to "text/xml"
      And add contact info
        | Ignite test | a@a.com | https://github.com/Apicurio/ |
      And add license "MIT License"
      And add tag "MyTag" with description "My desc"
      #TODO And create security scheme
      #TODO And create security requi
      And create a new path with link with name "MyPath by link"
      And create a new path with plus sign with name "MyPath by plus"

      And create a new data type with link with
        | NewDataLink | best data type ever | false |

      And create a new data type with plus sign with
        | NewDataPlus | best data type ever | false |

      And select path "/MyPath by link"
      And create new "GET" operation
      And create new "PUT" operation
      And create new "POST" operation
      And create new "DELETE" operation

      And select path "/MyPath by plus"
      And create new "GET" operation
      And create new "OPTIONS" operation
      And create new "HEAD" operation
      And create new "PATCH" operation

      Then save API as "json" and close editor
      When import API "tmp/download/openapi-spec.json"

      And check that API name is "MyApi"
      And check that API version is "2.20a"
      And check that API description is "New API desc"
      And check that API consume "text/xml application/json"
      And check that API produce "text/xml application/json"
      And check that API contact info is
        | Ignite test | a@a.com | https://github.com/Apicurio/ |

      And check that API license is "MIT License"
      And check that API have tag "MyTag" with description "My desc"

      And check that path "/MyPath by link" is created
      And check that path "/MyPath by plus" is created

      And check that data type "NewDataLink" is created
      And check that data type "NewDataPlus" is created

      And check that operation "GET" is created for path "/MyPath by link"
      And check that operation "PUT" is created for path "/MyPath by link"
      And check that operation "POST" is created for path "/MyPath by link"
      And check that operation "DELETE" is created for path "/MyPath by link"

      And check that operation "GET" is created for path "/MyPath by plus"
      And check that operation "OPTIONS" is created for path "/MyPath by plus"
      And check that operation "HEAD" is created for path "/MyPath by plus"
      And check that operation "PATCH" is created for path "/MyPath by plus"

      Then check all for errors


@search
  Scenario: test search on paths/datatypes
      And create a new path with plus sign with name "My paths 1"
      And create a new path with plus sign with name "My paths 2"
      And create a new path with plus sign with name "My paths x"
      And create a new path with plus sign with name "My paths24"
      And create a new path with plus sign with name "DataType1"

      And create a new data type with plus sign with
        | DataType1  | desc | false |

      And create a new data type with plus sign with
        | DataType2  | desc | false |

      And create a new data type with plus sign with
        | DataTypex  | desc | false |

      And create a new data type with plus sign with
        | DataType24 | desc | false |

      And search path or data type with substring "x"
      And check that path "/My paths x" is created
      And check that data type "DataTypex" is created
      And cancel searching

      And search path or data type with substring "2"
      And check that path "/My paths 2" is created
      And check that path "/My paths24" is created
      And check that data type "DataType2" is created
      And check that data type "DataType24" is created
      And cancel searching

      And search path or data type with substring "ATH"
      And check that path "/My paths 1" is created
      And check that path "/My paths 2" is created
      And check that path "/My paths x" is created
      And check that path "/My paths24" is created

      Then check all for errors


  @allGetParameters
  Scenario: fill all get parameters
    And create a new path with plus sign with name "MyGet"
    And create new "GET" operation

    Then select operation "GET"

    When set summary "MySummary"
    And set operation id "MyOperationID"
    And set description "MyDescription"
    And set tags "tag1,tag2"
    #TODO add override consumes and produces

    And set response 200 with clickable link
    And set response 100 with plus sign

    And set response description "Description for response 200" for response 200
    And set response type "Array" for response 200
    And set response type of "String" for response 200
    And set response type as "String" for response 200

    And set response description "Description for response 100" for response 100
    And set response type "Number" for response 100
    And set response type as "Float" for response 100
    #TODO add request body
    #TODO add query parameters
    #TODO add security requiremets

    And check that operation summary is "MySummary"
    And check that operation ID is "MyOperationID"
    And check that operation description is "MyDescription"
    And check that operation tags are "tag1,tag2"

    And check that exist response 200
    And check that description is "Description for response 200" for response 200
    And check that type is "Array" for response 200
    And check that type of is "String" for response 200
    And check that type as is "String" for response 200

    Then check all for errors


  @twoDataTypes
  Scenario: create 2 data types with/without rest
    And create a new data type with link with
      | data1 | best data type ever | false |

    And create a new data type with plus sign with
      | data2 | desc | true |

    And select data type "data1"
    And set data type description "best data type"
    And create data type property with name "MyProperty"
    And set description "My desc" for property "MyProperty"

    And set property type "Integer" for property "MyProperty"
    And set property type as "32-Bit Integer" for property "MyProperty"
    And set property "MyProperty" as required "true"

    And select data type "data2"
    And set data type example "MyExample"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"
    
    And check that path "/data2S" is created
    And check that operation "GET" is created for path "/data2S"
    And select path "/data2S"
    Then select operation "GET"
    And check that operation summary is "List All data2S"
    And check that operation ID is "getdata2S"
    And check that operation description is "Gets a list of all data2 entities."

    And check that exist response 200
    And check that description is "Successful response - returns an array of data2 entities." for response 200
    And check that type is "Array" for response 200
    And check that type of is "data2" for response 200

    And check that operation "POST" is created for path "/data2S"
    And select path "/data2S"
    Then select operation "POST"
    And check that operation summary is "Create a data2"
    And check that operation ID is "createdata2"
    And check that operation description is "Creates a new instance of a data2."

    And check that exist response 201
    And check that description is "Successful response." for response 201
    #Choose type mean that the type is not set
    And check that type is "Choose Type" for response 201

    #TODO check request body description and type

    And check that path "/data2S/{data2Id}" is created
    And check that operation "GET" is created for path "/data2S/{data2Id}"
    And select path "/data2S/{data2Id}"
    #TODO check that parameter exist
    Then select operation "GET"
    And check that operation summary is "Get a data2"
    And check that operation ID is "getdata2"
    And check that operation description is "Gets the details of a single instance of a data2."

    And check that exist response 200
    And check that description is "Successful response - returns a single data2." for response 200
    And check that type is "data2" for response 200

    And check that operation "PUT" is created for path "/data2S/{data2Id}"
    And select path "/data2S/{data2Id}"
    Then select operation "PUT"
    And check that operation summary is "Update a data2"
    And check that operation ID is "updatedata2"
    And check that operation description is "Updates an existing data2."

    And check that exist response 202
    And check that description is "Successful response." for response 202
    #Choose type mean that the type is not set
    And check that type is "Choose Type" for response 202

    And check that operation "DELETE" is created for path "/data2S/{data2Id}"
    And select path "/data2S/{data2Id}"
    Then select operation "DELETE"
    And check that operation summary is "Delete a data2"
    And check that operation ID is "deletedata2"
    And check that operation description is "Deletes an existing data2."

    And check that exist response 204
    And check that description is "Successful response." for response 204
    #Choose type mean that the type is not set
    And check that type is "Choose Type" for response 204

    Then check all for errors


  @pathWithParameters
  Scenario: create path with parameters and one override
    And create a new path with plus sign with name "test/{myId}"
    And create path parameter "myId"
    And set description "parameter myId desc" for path parameter "myId"

    And set path parameter type "String" for path parameter "myId"
    And set path parameter type as "Byte" for path parameter "myId"

    And create a new path with plus sign with name "test2/{id}/{api}"
    And create path parameter "id"
    And create path parameter "api"

    And set description "parameter id desc" for path parameter "id"
    And set path parameter type "String" for path parameter "id"
    And set path parameter type as "Date" for path parameter "id"


    And set description "parameter api desc" for path parameter "api"
    And set path parameter type "String" for path parameter "api"
    And set path parameter type as "Binary" for path parameter "api"

    And create new "GET" operation
    Then select operation "GET"
    And override parameter "id"
    And set description "override parameter id desc" for override path parameter "id" in operation

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And check that path "/test/{myId}" is created
    And check that path parameter "myId" is created for path "/test/{myId}"
    And check that path parameter "myId" has description "parameter myId desc"
    And check that path parameter "myId" has type "String" formatted as "Byte"

    And check that path "/test2/{id}/{api}" is created
    And check that path parameter "id" is created for path "/test2/{id}/{api}"
    And check that path parameter "api" is created for path "/test2/{id}/{api}"
    And check that path parameter "id" has description "parameter id desc"
    And check that path parameter "id" has type "String" formatted as "Date"
    And check that path parameter "api" has description "parameter api desc"
    And check that path parameter "api" has type "String" formatted as "Binary"

    And check that operation "GET" is created for path "/test2/{id}/{api}"
    Then select operation "GET"
    And check that path parameter "id" is overridden
    And check that overridden path parameter "id" has description "override parameter id desc"
    Then check all for errors

  Scenario: #kebab menu for paths and data types