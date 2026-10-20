# Finnace tracking app WIP

pretty basic tracking app. works on CSV files allowing to export them easily. 
## logInActivity

<img alt="{FC3A756F-A23F-4AAD-9BA2-1DB0C4F56E7F}" src="https://github.com/user-attachments/assets/1ed050f3-16f1-4d9f-880c-1136cbfc3e0b" />

checks the sql database for existing username email and password then validate those. it also saves whether the user wants the app to remember them and saves it to shered preferences for immediate login next time.

## signActivity

<img width="287" alt="{827BF00C-10B1-4368-A4D4-C90D30F24657}" src="https://github.com/user-attachments/assets/0ae763d1-290e-4488-a2cd-34c885ddf8cb" />
uses SQLite to store new user credentials.
## mainActicity

<img alt="{0A7903C8-01CE-4AEC-8DB7-64AD81A7F573}" src="https://github.com/user-attachments/assets/5cf19553-0211-471f-a421-2ff0c0ab3e1e" />

clicking on the balance allows to set it before the calculations and using the plus buttom to add transactions.
uses recycle view to hold the information. clicking on the item allows to view it while long clicking to delete it.

## settingsActivity

<img alt="{BAFAAC42-6E6E-4092-AF05-C77093F07271}" src="https://github.com/user-attachments/assets/510da4ef-536b-4ed4-913a-9a571d4b233c" />
includes options to clear data, delete account and sign out in addition to intent to debug activity and an export option.

## debugActivity

<img alt="{98385A8C-D302-497F-8868-E00614766E4E}" src="https://github.com/user-attachments/assets/56551d85-f96e-44bd-9cc3-5d29cede696a" />

has intents set to every other activity and an option to generate 10 random transactions and append them to the recycle view in main activity 
---


## Installation.
#On android

simply downloading and running the APK file from the realeses and pressing allow for the google play  scan
#On other Linux distros
Install waydroid then run
waydroid app install /path_to_download/release.apk
---

## WIP features:
- Optmizing preformance
