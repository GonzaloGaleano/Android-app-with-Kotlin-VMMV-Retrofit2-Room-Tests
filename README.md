# Android Kotlin / VMMV / Retrofit2 / Room / Tests
Android app example using VMMV architectural pattern.

## Run the app
 - To run the app clone the repository or download and uncompress it.
 - Open the project in android studio and run it.

## To run the tests
 - Select a specifict test and run it.
 - Or chose **Run 'All tests'** with contextual menu over app folder in the left project panel.

## Banck-end API
For this example i used https://jsonplaceholder.typicode.com

## Desing pattern
  The main design patter used is Material Design from (com.google.android.materia)

## Main Libraries
 - Retrofit for http request
 - Room for data persistence
  
## Benefits using the MVVM architecture with these libraries
 - The example app is based on Google recomendation: https://developer.android.com/topic/architecture#recommended-app-arch
 - I used viewBinding and dataBinding to pass some view controls to the view file.
 - The repository is based on a inteface and the viewModel use an inyected depencency of the repository instance, this allow an easy way to test the viewModel.
  
## Extra
 - The code include 2 examples of tests including viewModel test and liveData testing.
 - I added a splash screen for the first time loading the app.
