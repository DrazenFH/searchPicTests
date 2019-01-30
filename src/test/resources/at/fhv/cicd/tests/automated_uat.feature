Feature: Automated User Acceptance Tests for the Sentiment Analysis Application

  Background:
    Given Open https://fhv-application-staging.herokuapp.com/

  Scenario: Test login and logout
    Given Login with user 'user@test.com'
    When I press logout
    Then I see the login page

  Scenario: Test positive sentiment
    Given Login with user 'user@test.com'
    When Analyze the text 'I love people'
    Then The smiley should be happy
    And I press logout

  Scenario: Test negative sentiment
    Given Login with user 'user@test.com'
    When Analyze the text 'I hate people'
    Then The smiley should be unhappy
    And I press logout

  Scenario: Test neutral sentiment
    Given Login with user 'user@test.com'
    When Analyze the text ''
    Then The smiley should be neutral
    And I press logout

  Scenario: User interaction with history (video)
    Given Login with user 'user@test.com'
    When Analyze the text 'I love people'
    And Analyze the text 'I hate people'
    And Navigate to history
    Then The 1. row shows the history item with text 'I love people' and sentiment is 'happy'
    And The 2. row shows the history item with text 'I hate people' and sentiment is 'unhappy'
    And I press logout