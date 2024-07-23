How to use the library
This library simplifies getting the GPS location from an Android device
1. Download and review code for malware. Files to check are extention .kt. Optionally submit the files to VirusTotal.com for free inspection.
2. Update AGP steps as recommended
3. Run app on simulator, it should ask for GPS permission. On permission granted shows the location. Change location in sumulator, it updates in the UI
4. Update all dependencies in Gradle (Module :gpslibrary) optionally the module app
5. Change variant of gpslibrary to release (if you change app also then you will need to sign the app, library is does not need signing)
6. The library file gpslibrary-release.aar is located in the path /gpslibrary/build/outputs/aar/

Add the library to your project
1. Create directories under the app: /libs/
2. Drop the gpslibrary-release.aar into the directory
3. In your app project add the library to dependencies implementation(files("./libs/gpslibrary-release.aar"))
   Note: Some IDEs (like Android Studio) automatically configure Gradle to search common locations like libs. 

Implement library using kotlin activity and fragment
1. Add to android manifest in <application   android:name=".<name_of_file>" ex: "gpsApplication
2. Create file, I use Timber so add a few extra instructions and files
   a. CustomDebugTree
   b. Include the BuildConfig add to gradle in android {...  android.buildFeatures.buildConfig = true
