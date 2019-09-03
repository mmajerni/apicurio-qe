@apicuritoTests
@update
@updateTest
Feature: Update scenario test

  @skip_scenario_template
  @testOperatorUpdate
  Scenario: test update operator
    When deploy another custom resource
    Then check that apicurito has 6 pods
    And check that apicurito image is "mmajerni/apicurito-update:latest"