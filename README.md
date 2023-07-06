<img align="right" src="https://github.com/yamin335/PixMyWall/blob/master/demo_snap.gif" alt="Sample Output" width="300">

<h1>:national_park: PixMyWall</h1>

![Android](https://img.shields.io/badge/-Android-606060?style=flat&logo=android)
![Kotlin](https://img.shields.io/badge/-Kotlin-fff?style=flat&logo=kotlin)
![Android Studio](https://img.shields.io/badge/-Android_Studio-606060?style=flat&logo=androidstudio)

<h4>This project shows a sample workaround of how to perfectly load images from a paged api into android app using search query along with caching into 
  local database for offline usage. This app uses <a href="https://pixabay.com/">PIXABAY</a> photo search API as an online photo library. Below you can find a stack of
  technologies that have been used to develop this application. All features that the app currently have are also listed below along with the future improvement targets. To build and run the project please follow the guidelines below, because you can get errors due to the different configurations of Android Studio.</h4>

<br>

<h2>:building_construction: App Installation Guide:</h2>

#### Please follow the guideline described below to successfully run the project:

1. First `clone` the repository or download the `.zip` file of the repository and extract it.
2. Then delete the `.idea` hidden file from the `root` directory of the cloned project to configure the project with your environment.
3. Since this project uses `jdk 17`, So, as a next step you have to select the `jdk 17` from project configuration. To do that, please follow the guidelines below:
   - Open `Settings` from `Android Studio` -> Click & expand `Build, Execution, Deployment` -> Click & expand `Build Tools`.
   - Click `Gradle` and select `jbr-17` or `coretto-17` as Gradle JDK. If you can't find any of the mentioned `JDK` there, please download one.
4. Sync the project now.
5. Open `local.properties` file and place this line `API_KEY = "37699167-c79ddc6ba4895b131ee74e6f7"` under the `sdk.dir="..."` declaration.
6. That's it 😊 now Clean & Run the project, you will see a nice UI 😊❤️

### N.B: For a quick look, you can install the `.apk` file directly in your phone by downloading it from the [release](https://github.com/yamin335/PixMyWall/releases/tag/v1.0.0) folder. 

<br>

<h2>🛠️ Tech Stack</h2>

- Single Activity Architecture
- MVVM Architecture
- Android Architecture Components
- Databinding and ConstraintLayout
- Kotlin Coroutines
- Hilt Dependency Injection
- Room Database
- Material Design Components
- Retrofit Network Library
- Android Paging 3
- Coil Image Loader
- Android Navigation Components

<h2>🍀 App Features</h2>

- Default image search for "fruits"
- List of images as a search result
- Offline search
- Details info of images
- HD quality image in details
- Details show confirmation
- App exit confirmation
- Full screen view of HD image with pinch zoom
- Automatic HD image loading in response of network availability
- Automatic response to network
- Automatic refresh of search result when network restores
- Smooth handling of configuration changes
- Restore states after configuration changes
- Splash screen
- Dynamic changes in number of images per row depending on the oriyentation of the scren

<h2>🧐 Future Improvements</h2>

- Implementation of unit and instrumental tests
- Image download options
- Fully configuration of night or dark theme

<h2>✨ Catch up ✨</h2>

- 🧐 If you find anything wrong then please create an [Issue](https://github.com/yamin335/PixMyWall/issues/new)
- ⭐️ If you find this project helpful then don't forget to star it
- 🍀 Meanwhile You can also checkout my blogs on [Medium](https://medium.com/@mdyamin)
- 🥰 Feel free to visit my other [Repositories](https://github.com/yamin335?tab=repositories), there might be something waiting for you
