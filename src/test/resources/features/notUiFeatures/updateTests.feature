@apicuritoTests
@update
@notUi
Feature: Update scenario test

  @updateCustomResource
  Scenario: test update custom resource
    When deploy "second" custom resource
    Then check that "GENERATOR" has "2" pods
    And check that "SERVICE" has "2" pods
    Then reinstall apicurito

  @updateOperator
  Scenario: test update operator
    When install older version of apicurito
    When update operator to the new version
    And check that "SERVICE" has "1" pods
    Then check that apicurito "operator" is "default"
    And check that apicurito "image" is "default"
    Then reinstall apicurito
