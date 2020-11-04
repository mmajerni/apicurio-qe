@apicuritoTests
@update
Feature: Update scenario test

  @updateCustomResource
  Scenario: test update custom resource
    When deploy "second" custom resource
    Then check that "apicurito-service-generator" has 4 pods
    And check that "apicurito-service-ui" has 4 pods
    Then reinstall apicurito

  @updateOperator
  Scenario: test update operator
    When deploy another operator with ui image "mmajerni/apiuiupdate:latest"
    Then check that apicurito "operator" is "mmajerni/apiopupdate:latest"
    And check that apicurito "image" is "mmajerni/apiuiupdate:latest"
    Then reinstall apicurito
