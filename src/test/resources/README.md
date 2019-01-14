# Description of tests structure
Tests structure consist of 6 steps classes where are all Gherkin steps.

Four of them (`DataTypeSteps`, `MainPageSteps`, `OperationSteps`, `PathSteps`) contains steps related to one specific page.

Class `CommonSteps` contains steps which can be used on every page.

Class `VerificationSteps` contains all verification steps related to all pages.

Utils classes have support function to their step classes.
