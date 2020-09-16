@apicuritoTests
@operatorhub
Feature: OperatorHub installation test

  Scenario: test operatorhub installation
    When delete running instance of apicurito
    When deploy operator from operatorhub
    Then check that apicurito operator is deployed and in running state

    When patch ClusterServiceVersion with UI image "mmajerni/apiui:latest"
    And deploy "first" custom resource
    Then check that apicurito "image" is "mmajerni/apiui:latest"
    And check that apicurito has 4 pods
    Then clean openshift after operatorhub test
    Then reinstall apicurito