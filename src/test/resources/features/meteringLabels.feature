@apicuritoTests
@check-apicurito-metering-labels
  Feature: Check metering labels
    Scenario: Check that metering labels have correct values
      When check that "fuse-apicurito" has 1 pods
      And check that "apicurito-service-generator" has 3 pods
      And check that "apicurito-service-ui" has 3 pods
      Then check that metering labels have correct values for "OPERATOR"
      And check that metering labels have correct values for "SERVICE"
      And check that metering labels have correct values for "GENERATOR"
