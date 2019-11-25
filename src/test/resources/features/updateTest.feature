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

    @testOperatorhub
    Scenario: test operatorhub
      When deploy apicurito into operatorhub and subscribe
      Then check that apicurito operator is deployed and in running state
