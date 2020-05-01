# mastermind
Mastermind board game - Android implementation
Built for interview challenge

In order to build and run you will need:
1. Android Studio version 3.6.2
2. Gradle version 3.6.2
3. JDK 
Build the project in Android Studio and deploy on emulator or real device. 
If there is any problems with build, you can alternatively deploy apk and aab files that you can find in root folder

Project is built using Cleen Architectural Pattern and separation of functionality into different layers -
1. Data
2. Domain
3. Framework
4. Presentation
5. Usecases

Dependencies are provided by Dependency Injection by Dagger. 
Saving data on device implemented via SharedPreferences 
Usecases and viewmodels are tested via Unit tests
Functionality includes Single Player and Multiplayer mode
Settings screen and How to play screen are planned but not implemented due to time constraints.
