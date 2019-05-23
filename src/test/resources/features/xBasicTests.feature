@apicuritoTests
@basicTests
Feature: Basic tests

  Background:
    Given delete API "tmp/download/openapi-spec.json"
    And delete API "tmp/download/openapi-spec.yaml"
    And log into apicurito

  @exportImportJson
  Scenario: export and import as json
    When import API "src/test/resources/preparedAPIs/basic.json"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API name is "testAPI"

  @exportImportYaml
  Scenario: export and import as yaml
    When import API "src/test/resources/preparedAPIs/basic.yaml"
    And save API as "yaml" and close editor
    And import API "tmp/download/openapi-spec.yaml"
    Then check that API name is "testAPI"

  @createPathByLink
  Scenario: create path by link
    When create a new API
    And create a new path with link
      | MyPathByLink | true |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that path "/MyPathByLink" is created

  @createPathByPlus
  Scenario: create path by plus
    When create a new API
    And create a new path with link
      | MyPathByPlus | false |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that path "/MyPathByPlus" is created

  @createDataTypeByLink
  Scenario: create data type by link
    When create a new API
    And create a new data type by link
      | NewDataLink | best data type ever | false | true |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that data type "NewDataLink" is created

  @createDataTypeByPlus
  Scenario: create a new data type by plus
    When create a new API
    And create a new data type by link
      | NewDataPlus | best data type ever | false | false |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that data type "NewDataPlus" is created

  @createPutOperation
  Scenario: create PUT operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create new "PUT" operation
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that operation "PUT" is created for path "/clearPath"

  @createPostOperation
  Scenario: create POST operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create new "POST" operation
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that operation "POST" is created for path "/clearPath"

  @createDeleteOperation
  Scenario: create PUT operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create new "DELETE" operation
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that operation "DELETE" is created for path "/clearPath"

  @createOptionsOperation
  Scenario: create OPTIONS operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create new "OPTIONS" operation
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that operation "OPTIONS" is created for path "/clearPath"

  @createHeadOperation
  Scenario: create HEAD operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create new "HEAD" operation
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that operation "HEAD" is created for path "/clearPath"

  @createPatchOperation
  Scenario: create PATCH operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create new "PATCH" operation
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that operation "PATCH" is created for path "/clearPath"

  @createTraceOperation
  Scenario: create TRACE operation
    When import API "src/test/resources/preparedAPIs/basic.json"
    And select path "/clearPath"
    And create new "TRACE" operation
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that operation "TRACE" is created for path "/clearPath"