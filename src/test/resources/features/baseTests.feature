@apicuritoTests
@baseTests
Feature: Basic tests

  Background:
    Given delete API "tmp/download/openapi-spec.json"
    And log into apicurito
    And create a new API
    And initialize collector

  @base
  Scenario: Test on basic concepts
    When create a new path with link
      | MyPathByLink | true  |
      | MyPathByPlus | false |

    And create a new data type by link
      | NewDataLink | best data type ever | false | true  |
      | NewDataPlus | best data type ever | false | false |

    And select path "/MyPathByLink"
    And create new "GET" operation
    And create new "PUT" operation
    And create new "POST" operation
    And create new "DELETE" operation

    And select path "/MyPathByPlus"
    And create new "GET" operation
    And create new "OPTIONS" operation
    And create new "HEAD" operation
    And create new "PATCH" operation
    And create new "TRACE" operation

    Then check that path "/MyPathByLink" is created
    And check that path "/MyPathByPlus" is created

    And check that data type "NewDataLink" is created
    And check that data type "NewDataPlus" is created

    And check that operation "GET" is created for path "/MyPathByLink"
    And check that operation "PUT" is created for path "/MyPathByLink"
    And check that operation "POST" is created for path "/MyPathByLink"
    And check that operation "DELETE" is created for path "/MyPathByLink"

    And check that operation "GET" is created for path "/MyPathByPlus"
    And check that operation "OPTIONS" is created for path "/MyPathByPlus"
    And check that operation "HEAD" is created for path "/MyPathByPlus"
    And check that operation "PATCH" is created for path "/MyPathByPlus"
    And check that operation "TRACE" is created for path "/MyPathByPlus"

    Then check all for errors

  @importExport
  Scenario: Test import and export as json/yaml
    When create a new path with link
      | ImportExportJson | false |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that path "/ImportExportJson" is created

    When create a new path with link
      | ImportExportYaml | false |

    And save API as "yaml" and close editor
    And import API "tmp/download/openapi-spec.yaml"
    Then check that path "/ImportExportJson" is created
    And check that path "/ImportExportYaml" is created

    Then check all for errors
