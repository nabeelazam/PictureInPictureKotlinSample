**Task 1 - Legacy support**

I kept this task at the end but haven’t attempted as it requires major refactoring.

**Task 2 - Unit tests**

Initially I started with this task and added libraries for testing following
- Mockito - mockito-core, mockito-inline, mockk
- LiveData - core-testing
- Coroutines - kotlinx-coroutine-test

Couple of things that I had to consider
- Accessing SystemClock for unit test
    - Instead of using system clock directly I created FakeClock and provided  it to ViewModel
- Testing Live data
    - Mocked startedObserve Observer<Boolean> to capture the changes from LiveData. To verify the sequence
      used InOrder to confirm onChanged(true) was called before onChange(false).

Note: I have left the test commented as I have to make changes for the next task. Currently test that I have added for "startOrPause" method doesn't work.


**Task 3 - New feature**

For this task I explored multiple ways to achieve this. Initially started with saving state to object globally available
- Started with saving timer state to global object(singleton)
- Hoisting the timer state into view model and saving to preferences
- Adding repository for the timer state and injecting into ViewModel (ideally via DI)

Since task requires to have a repository that can be plugged in other parts of the app. So I chose to have a repository that holds the state of the timer (started, millis) and injected into the view model.
Ideally this should have been provided by DI, to keep it simple I manually made the repository singleton and injected into the ViewModel.

Step by step explanation:
- Created repository to hold timer state
- Injected/passed into the view Model
- Added MainViewModelFactory to provide view model
- Created CustomClock and injected into the ViewModel for testing purposes

Issues:
- Challenge to test functionality that use SystemClock
- Testing code including coroutines (i.e. awaitFrame())

Improvements/Refactor:
- Provide CoroutineDispatcher in ViewModel and TestCoroutineDispatcher for tests
- Update/refactor start() to avoid starting new coroutine everytime 