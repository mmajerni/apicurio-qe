@apicuritoTests
@ui
@pathsTests
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
    And create "query" on "operations" page with plus sign "false"
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that exist "query" on "operations" page
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

  @createOperationHP
  Scenario: create header parameter for operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"
    And create "header" on "operations" page with plus sign "true"
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that exist "header" on "operations" page
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
    And create "RFD" on "operations" page with plus sign "false"
      | FormData | Description for FormData | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"
    Then check that exist "RFD" on "operations" page
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

  @createPathQP
  Scenario: create query parameter on path page
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create "query" on "path" page with plus sign "true"
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/clearPath"
    Then check that exist "query" on "path" page
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

  @createPathHP
  Scenario: create header parameter on path page
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create "header" on "path" page with plus sign "false"
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/clearPath"
    Then check that exist "header" on "path" page
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

  @allGetParameters
  Scenario: fill almost all get parameters
    # There are missing: request data form, override parameter and security requirement
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/operations"
    And select operation "GET"

    And set operation summary "MySummary"
    And set operation id "MyOperationID"
    And set operation description "MyDescription"
    And set operation tags "tag1,tag2"
    And override consumes with "text/xml" for operation "GET"
    And override produces with "text/xml" for operation "GET"

    And create "query" on "operations" page with plus sign "false"
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

    And create "header" on "operations" page with plus sign "false"
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

    And create request body
    And set request body description "Description for RB"
    And set request body type "Array"
    And set request body type of "String"
    And set request body type as "Date"

    And set response 100 with plus sign
    And set response description "Description for response 100" for response 100
    And set response type "Number" for response 100
    And set response type as "Float" for response 100

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/operations"
    And select operation "GET"

    Then check that operation summary is "MySummary"
    And check that operation ID is "MyOperationID"
    And check that operation description is "MyDescription"
    And check that operation tags are "tag1,tag2"
    And check that operation consumes "text/xml"
    And check that operation produces "text/xml"

    And check that exist "query" on "operations" page
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

    And check that exist "header" on "operations" page
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

    And check that exist request body
    And check that request body description is "Description for RB"
    And check that request body type is "Array"
    And check that request body type of is "String"
    And check that request body type as is "Date"

    And check that exist response 100
    And check that description is "Description for response 100" for response 100
    And check that type is "Number" for response 100
    And check that type as is "Float" for response 100

  @otherParameters
  Scenario: fill other operation parameters
    When import API "src/test/resources/preparedAPIs/paramsAndSecurity.json"
    And select path "/first/{id}/{name}{email}"
    And select operation "POST"

    And create "RFD" on "operations" page with plus sign "false"
      | FormData | Description for FormData | Required | Array | String | Date |

    And override security requirements in operation with
      | oauth |
      | basic |

    And override security requirements in operation with
      | No Security |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"
    And select path "/first/{id}/{name}{email}"
    And select operation "POST"

    Then check that exist "RFD" on "operations" page
      | FormData | Description for FormData | Required | Array | String | Date |

    And check that operation security requirement "oauth, basic" exist
    And check that operation security requirement "No Security" exist

  @pathWithParameters
  Scenario: create path with parameters, one override and one create in operation
    When import API "src/test/resources/preparedAPIs/paramsAndSecurity.json"
    And select path "/first/{id}/{name}{email}"

    And create path parameter "email"
    And set description "parameter email desc" for path parameter "email"
    And set path parameter type "String" for path parameter "email"
    And set path parameter type as "Byte" for path parameter "email"

    And select operation "GET"
    And override parameter "email"
    And set description "override parameter email desc" for override path parameter "email" in operation
    #TODO create parameter in operation

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/first/{id}/{name}{email}"

    Then check that path parameter "email" is created for path "/first/{id}/{name}{email}"
    And check that path parameter "email" has description "parameter email desc"
    And check that path parameter "email" has type "String"
    And check that path parameter "email" has type as "Byte"

    And select operation "GET"

    And check that path parameter "email" is overridden
    And check that overridden path parameter "email" has description "override parameter email desc"