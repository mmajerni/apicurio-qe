@apicuritoTests
@pathsTestsX
Feature: Path tests

  Background:
    Given delete API "tmp/download/openapi-spec.json"
    And log into apicurito

  @setOperationSummary
  Scenario: set operation summary
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And set operation summary "MySummary"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that operation summary is "MySummary"

  @sethOperationID
  Scenario: set operation id
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And set operation id "MyOperationID"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that operation ID is "MyOperationID"

  @sethOperationDescription
  Scenario: set operation description
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And set operation description "MyDescription"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that operation description is "MyDescription"

  @setOperationTags
  Scenario: set operation tags
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And set operation tags "tag1,tag2"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that operation tags are "tag1,tag2"

  @overrideConsumes
  Scenario: override consumes in operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    #TODO cut "for operation"
    And override consumes with "text/xml" for operation "GET"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that operation consumes "text/xml"

  @overrideProduces
  Scenario: override produces in operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    #TODO cut "for operation"
    And override produces with "text/xml" for operation "GET"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that operation produces "text/xml"

  @createOperationQP
  Scenario: create query parameter for operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And create query parameters
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that exist query parameters
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

  @createOperationHP
  Scenario: create header parameter for operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And create header parameters
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that exist header parameters
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

  @createOperationRequestBody
  Scenario: create request body
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"

    And create request body
    And set request body description "Description for RB"
    And set request body type "Array"
    And set request body type of "String"
    And set request body type as "Date"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"

    Then check that exist request body
    And check that request body description is "Description for RB"
    And check that request body type is "Array"
    And check that request body type of is "String"
    And check that request body type as is "Date"

  @createOperationRDF
  Scenario: create request data form
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And create request form data
      | FormData | Description for FormData | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that exist request form data
      | FormData | Description for FormData | Required | Array | String | Date |

  @createOperationResponseLink
  Scenario: create response by link
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"

    And set response 200 with clickable link

    And set response description "Description for response 200" for response 200
    And set response type "Array" for response 200
    And set response type of "String" for response 200
    And set response type as "String" for response 200

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"

    Then check that exist response 200
    And check that description is "Description for response 200" for response 200
    And check that type is "Array" for response 200
    And check that type of is "String" for response 200
    And check that type as is "String" for response 200

  @createOperationResponsePlus
  Scenario: create response by plus
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"

    And set response 100 with plus sign
    And set response description "Description for response 100" for response 100
    And set response type "Number" for response 100
    And set response type as "Float" for response 100

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"

    Then check that exist response 100
    And check that description is "Description for response 100" for response 100
    And check that type is "Number" for response 100
    And check that type as is "Float" for response 100

  @overrideSecurityRequirements
  Scenario: override security requirements
    When import API "src/test/resources/preparedAPIs/paramsAndSecurity.json"
    And select path "/first/{id}"
    And select operation "GET"

    And override security requirements in operation with
      | oauth |
      | api   |

    And override security requirements in operation with
      | No Security |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/first/{id}"
    And select operation "GET"
    Then check that operation security requirement "oauth, api" exist
    And check that operation security requirement "No Security" exist

  @createPathParameter
  Scenario: create one path parameter
    When import API "src/test/resources/preparedAPIs/paramsAndSecurity.json"
    And select path "/first/{id}"

    And create path parameter "id"
    And set description "parameter id desc" for path parameter "id"
    And set path parameter type "File" for path parameter "id"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/first/{id}"

    Then check that path parameter "id" is created for path "/first/{id}"
    And check that path parameter "id" has description "parameter id desc"
    And check that path parameter "id" has type "File"

  @overridePathParameter
  Scenario: override path parameter
    When import API "src/test/resources/preparedAPIs/paramsAndSecurity.json"
    And select path "/first/{id}/{name}{email}"

    And override parameter "id"
    And set description "override parameter id desc" for override path parameter "id" in operation

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/first/{id}/{name}{email}"

    Then check that path parameter "id" is overridden
    And check that overridden path parameter "id" has description "override parameter id desc"

    #TODO Add test -> create path parameter in operation
    #TODO Add tests -> create path QP and HP