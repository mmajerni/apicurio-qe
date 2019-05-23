@apicuritoTests
@mainPageTests
Feature: Test everything on main page

  Background:
    Given delete API "tmp/download/openapi-spec.json"
    And log into apicurito
    And create a new API
    And initialize collector

  @apiPage
  Scenario: Test the api main page (set everything expect security)
    When change API name to "MyApi"
    And set API version to "2.20a"
    And change description to "New API desc"
    And set consumes to "text/xml"
    And set produces to "text/xml"
    And add contact info
      | Ignite test | a@a.com | https://github.com/Apicurio/ |

    And add license "MIT License"
    And add tag "MyTag" with description "My desc"

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    And check that API name is "MyApi"
    And check that API version is "2.20a"
    And check that API description is "New API desc"
    And check that API consume "text/xmlapplication/json"
    #TODO add space and check every label separately
    And check that API produce "text/xmlapplication/json"
    And check that API contact info is
      | Ignite test | a@a.com | https://github.com/Apicurio/ |

    And check that API license is "MIT License"
    And check that API have tag "MyTag" with description "My desc"

    Then check all for errors

  @search
  Scenario: test search on paths/datatypes
    When create a new path with link
      | Mypaths1  | false |
      | Mypaths2  | false |
      | Mypathsx  | false |
      | Mypaths24 | false |
      | DataType1 | false |

    And create a new data type by link
      | DataType1  | desc | false | false |
      | DataType2  | desc | false | false |
      | DataTypex  | desc | false | false |
      | DataType24 | desc | false | false |

    And search path or data type with substring "x"
    Then check that path "/Mypathsx" is created
    And check that data type "DataTypex" is created

    When search path or data type with substring "2"
    Then check that path "/Mypaths2" is created
    And check that path "/Mypaths24" is created
    And check that data type "DataType2" is created
    And check that data type "DataType24" is created

    When search path or data type with substring "ATH"
    Then check that path "/Mypaths1" is created
    And check that path "/Mypaths2" is created
    And check that path "/Mypathsx" is created
    And check that path "/Mypaths24" is created

    Then check all for errors

  @security
  Scenario: test security schemes and requirements
    When create basic security scheme with values
      | BASIC | Basic scheme description |

    And create API Key security scheme with values
      | APIkeySecurityName | API Key description | HTTP header | ParameterName |
      | APIkeyWarnings     |                     |             |               |

    And create OAuth security scheme with values
      | OAuth2name     | OAuth2 description | Access Code | https://github.com/Apicurio/ | https://github.com/Apicurio/apicurito |
      | OAuth2warnings |                    |             |                              |                                       |

    And add scopes to scheme "OAuth2name"
      | firstScope  | firstScopeDescription  |
      | secondScope | secondScopeDescription |

    And create security requirement with schemes
      | OAuth2name         |
      | APIkeySecurityName |

    And create security requirement with schemes
      | OAuth2name |
      | BASIC      |

    And add scopes to security requirement "OAuth2name, APIkeySecurityName" and OAuth scheme "OAuth2name"
      | firstScope |

    Then save API as "json" and close editor
    When import API "tmp/download/openapi-spec.json"

    Then check security scheme
      | BASIC              | basic  | Basic scheme description |
      | APIkeySecurityName | apiKey | API Key description      |
      | APIkeyWarnings     | apiKey |                          |
      | OAuth2name         | oauth2 | OAuth2 description       |
      | OAuth2warnings     | oauth2 |                          |

    And check that API security requirement "OAuth2name, APIkeySecurityName" exists
    And check that API security requirement "OAuth2name, BASIC" exists

#    And check apiKey security scheme   //TODO cannot be done now (//TODO check in Source tab???)
#      | APIkeySecurityName | API Key description | HTTP header | ParameterName |

#    And check oauth security scheme   //TODO cannot be done now (//TODO check in Source tab???)
#      | OAuth2name     | OAuth2 description | Access Code | https://github.com/Apicurio/ | https://github.com/Apicurio/apicurito |

    Then check all for errors

  Scenario: #kebab menu for paths and data types