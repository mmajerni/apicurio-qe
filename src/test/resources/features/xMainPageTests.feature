@apicuritoTests
@mainPageTestsX
Feature: Main page tests

  Background:
    Given delete API "tmp/download/openapi-spec.json"
    And log into apicurito
    And create a new API

  @changeAPIname
  Scenario: change API name
    When change API name to "MyApi"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API name is "MyApi"

  @setAPIversion
  Scenario: set API version
    When set API version to "2.20a"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API version is "2.20a"

  @setAPIdescription
  Scenario: set API description
    When change description to "New API desc"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API description is "New API desc"

  @setAPIconsumes
  Scenario: set API consumes
    When set consumes to "text/xml"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API consume "text/xmlapplication/json"
    #TODO add space and check every label separately

  @setAPIproduces
  Scenario: set API produces
    When set produces to "text/xml"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API produce "text/xmlapplication/json"
    #TODO add space and check every label separately

  @setAPIcontact
  Scenario: set API contact
    When add contact info
      | Ignite test | a@a.com | https://github.com/Apicurio/ |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API contact info is
      | Ignite test | a@a.com | https://github.com/Apicurio/ |

  @setAPIlicense
  Scenario: set API license
    When add license "MIT License"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API license is "MIT License"

  @setAPItag
  Scenario: set API tag
    When add tag "MyTag" with description "My desc"
    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check that API have tag "MyTag" with description "My desc"

  @createAPIsecurityBasic
  Scenario: create basic security scheme for API
    When create basic security scheme with values
      | BASIC | Basic scheme description |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check security scheme
      | BASIC | basic | Basic scheme description |

  @createAPIsecurityAPIkey
  Scenario: create API Key security scheme for API
    When create API Key security scheme with values
      | APIkeyWarnings |  |  |  |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check security scheme
      | APIkeyWarnings | apiKey |  |

  @createAPIsecurityOauth2
  Scenario: create OAuth2 security scheme for API
    When create OAuth security scheme with values
      | OAuth2warnings |  |  |  |  |

    And save API as "json" and close editor
    And import API "tmp/download/openapi-spec.json"
    Then check security scheme
      | OAuth2warnings | oauth2 |  |