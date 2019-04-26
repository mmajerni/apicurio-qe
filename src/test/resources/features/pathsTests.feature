@apicuritoTests
@pathsTests
Feature: Test everything on paths and operations pages

  Background:
    Given delete API "tmp/download/openapi-spec.json"
    And log into apicurito
    And create a new API
    And initialize collector

  @allGetParameters
  Scenario: fill almost all get parameters
    When create a new path with link
      | MyGet | false |

    And create new "GET" operation

    Then select operation "GET"

    When set operation summary "MySummary"
    And set operation id "MyOperationID"
    And set operation description "MyDescription"
    And set operation tags "tag1,tag2"

    #And override consumes with "text/xml" for operation "GET"      //https://github.com/Apicurio/apicurito/issues/35
    #And override produces with "text/xml" for operation "GET"

    And set response 200 with clickable link
    And set response 100 with plus sign

    And set response description "Description for response 200" for response 200
    And set response type "Array" for response 200
    And set response type of "String" for response 200
    And set response type as "String" for response 200

    And set response description "Description for response 100" for response 100
    And set response type "Number" for response 100
    And set response type as "Float" for response 100

    And create request body
    And set request body description "Description for RB"
    And set request body type "Array"
    And set request body type of "String"
    And set request body type as "Date"

    And create query parameters
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

    And create header parameters
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And select path "/MyGet"
    And select operation "GET"
    Then check that operation summary is "MySummary"
    And check that operation ID is "MyOperationID"
    And check that operation description is "MyDescription"
    And check that operation tags are "tag1,tag2"
    #And check that operation consumes "text/xml"
    #And check that operation produces "text/xml"

    And check that exist response 200
    And check that description is "Description for response 200" for response 200
    And check that type is "Array" for response 200
    And check that type of is "String" for response 200
    And check that type as is "String" for response 200

    And check that exist response 100
    And check that description is "Description for response 100" for response 100
    And check that type is "Number" for response 100
    And check that type as is "Float" for response 100

    And check that exist query parameters
      | QueryParameter | Description for Query Param | Required | Array | String | Date |

    And check that exist header parameters
      | HeaderParameter | Description for Header Param | Required | Array | String | Date |

    And check that exist request body
    And check that request body description is "Description for RB"
    And check that request body type is "Array"
    And check that request body type of is "String"
    And check that request body type as is "Date"

    Then check all for errors

  @otherParameters
  Scenario: fill other operation parameters
    When create basic security scheme with values
      | BASICscheme  | Basic scheme description  |
      | secondScheme | Second scheme description |

    And create a new path with link
      | MyPost | false |

    And create new "POST" operation
    Then select operation "POST"
    And create request form data
      | FormData | Description for FormData | Required | Array | String | Date |

    And override security requirements in operation with
      | BASICscheme  |
      | secondScheme |

    And override security requirements in operation with
      | No Security |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And check security scheme
      | BASIC scheme  | basic | Basic scheme description  |
      | second scheme | basic | Second scheme description |

    And check that path "/MyPost" is created
    And check that operation "POST" is created for path "/MyPost"

    And check that exist request form data
      | FormData | Description for FormData | Required | Array | String | Date |

    And check that operation security requirement "BASICscheme, secondScheme" exist
    And check that operation security requirement "No Security" exist

    Then check all for errors


  @pathWithParameters
  Scenario: create path with parameters and one override
    When create a new path with link
      | test/{myId} | false |

    And create path parameter "myId"
    And set description "parameter myId desc" for path parameter "myId"

    And set path parameter type "String" for path parameter "myId"
    And set path parameter type as "Byte" for path parameter "myId"

    And create a new path with link
      | test2/{id}/{api} | false |

    And create path parameter "id"
    And create path parameter "api"

    And set description "parameter id desc" for path parameter "id"
    And set path parameter type "String" for path parameter "id"
    And set path parameter type as "Date" for path parameter "id"


    And set description "parameter api desc" for path parameter "api"
    And set path parameter type "String" for path parameter "api"
    And set path parameter type as "Binary" for path parameter "api"

    And create new "GET" operation
    And select operation "GET"
    And override parameter "id"
    And set description "override parameter id desc" for override path parameter "id" in operation

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    Then check that path "/test/{myId}" is created
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
    When select operation "GET"
    Then check that path parameter "id" is overridden
    And check that overridden path parameter "id" has description "override parameter id desc"
    Then check all for errors