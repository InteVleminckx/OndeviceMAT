# O-MAT: On-Device Mobile Application Testing: Reproduction Package

This package provides all necessary steps to reproduce the results of the paper On-Device Mobile Application Testing (O-MAT).

## Project Installation

1) Clone the project using the GitHub repository: https://github.com/InteVleminckx/OndeviceMAT
2) Open the project in your preferred IDE (e.g., Android Studio).
3) The Gradle files will automatically build upon opening the project (if using Android Studio). be build (in case when using Android Studio)
4) Once the project has been successfully built, the installation is complete.

## Installing the evaluated applications

1) Connect a mobile device to your computer. This can be a physical device connected via USB or over the network, or an emulator.
2) Use the command ```adb devices``` in your terminal to verify that the device is connected.
3) Navigate to the evaluated/applications directory in the project folder.
4) This folder contains nine applications. Install each application using the command: ```adb install <application_name>.apk```

## Installing the O-MAT application

1) With the project successfully built and a device connected, you can install the O-MAT app by running it directly from your IDE.
2) The application should launch on the mobile device.
3) You can now stop the application in the IDE and close any windows that were opened by the application.
4) The O-MAT app is now fully installed on the device and can be launched directly from the mobile device. It no longer needs to be connected to the computer.## Running test cases

# Running Test Cases

1) Launch the O-MAT application. You will be prompted to grant certain permissions.
2) The first permission required is for the accessibility service. The app will automatically open the relevant settings window. Enable "On-device MAT access service" and return to the previous screen by pressing the arrow at the top left.
   - Ensure the text under "On-device MAT access service" says "on". If you encounter a failure message, disable and re-enable the service. It should resolve the issue.
3) Once back in the app, a pop-up will prompt you to start recording. Press "Start Now".
4) Next, grant the required file permissions and press the top-left arrow again (only necessary once).
5) A list of all installed applications will appear (installed in the previous step "installing the evaluated applications"). Select one of these applications to begin testing. After 10 seconds, the test will automatically start.
6) Once the test is complete, the logs will be saved in the TestLogs folder located in the Documents directory of the device (Files application).
7) Note: To start a new test, first close the O-MAT application and disable the accessibility service (can be found under settings > accessibility). Then repeat the process from step 1 in this section.

# Running the Test Cases for emulator and the Server-client model
1) To execute these test cases, you will need an additional repository: https://anonymous.4open.science/r/OffDeviceTesting-4606/
2) The repository contains a README file with detailed instructions on how to run and add test cases.
