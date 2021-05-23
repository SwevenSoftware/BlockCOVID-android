![Build status](https://github.com/SwevenSoftware/BlockCOVID-android/actions/workflows/build-app.yml/badge.svg)
# BlockCOVID-android
## Description
Android module for the project BlockCOVID.
This module provides the Android application used by the users and cleaners to reserve a desk and clean rooms.

## Usage
### Prerequisites
This module requires an instance of the [server](https://github.com/SwevenSoftware/BlockCOVID-server) module running. By default the app tries to redirect all api requests to "http://192.168.210.30:8091", you should change the default url inside [NetworkClient.kt](https://github.com/SwevenSoftware/BlockCOVID-android/blob/develop/app/src/main/java/com/sweven/blockcovid/services/NetworkClient.kt) for the app to work properly.

### Building and running
For building and running instructions, refer to the section ["Installazione"](https://swevensoftware.github.io/manutentore/android/installazione.html) of the Android Developer Manual.

## Contributing
We adopt a [Gitflow workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow).
So in order to contribute to the application the steps are:
- start from `develop` branch
- `git flow feature start [faeture name]` (alternatively `git checkout -b feature/[feature name]`)
- Implement the new feature and the corresponding tests
- commit your changes
- `git flow feature pulish [feature name]` (alternatively `git push -u origin feature/[feature name]`)
- open a pull request describing your changes and addressing issues if necessary
eventually an administrator will review your work and merge it in the develop branch.