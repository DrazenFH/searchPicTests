Feature: Automated User Acceptance Tests for the Search Pic Application

  Background:
    Given Open https://searchpic.herokuapp.com/

  Scenario: Test login
    Given Login with user 'user@test.com'

  Scenario: Test no search request
    Given Login with user 'user@test.com'
    When Search the picture ''
    Then The result list should be 'jpg'

  Scenario: Test with search request
    Given Login with user 'user@test.com'
    When Search the picture 'dog'
    Then The result list should be 'jpg'

