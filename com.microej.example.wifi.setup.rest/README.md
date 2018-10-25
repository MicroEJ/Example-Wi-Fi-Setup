# Overview

This library mounts a rest server to provide the Access Point credential to join a Wi-Fi.
It has three Endpoints:
 - /join (see [com.microej.example.wifi.setup.rest.endpoint.JoinEndPoint](src/main/java/com/microej/example/wifi/setup/rest/endpoint/JoinEndPoint.java))
 - /scan (see [com.microej.example.wifi.setup.rest.endpoint.ScanEndPoint](src/main/java/com/microej/example/wifi/setup/rest/endpoint/ScanEndPoint.java))
 - /diagnostic (see [com.microej.example.wifi.setup.rest.endpoint.DiagnosticEndPoint](src/main/java/com/microej/example/wifi/setup/rest/endpoint/DiagnosticEndPoint.java))

# Usage
See dependencies section to provide required libraries for build.
## Run
This example provides a code sample that can be run.
Configure the SoftAP configuration in [Main.java](src\main\java\com\microej\example\wifi\setup\rest\Main.java)

### Build
1. Right Click on [Main.java](src\main\java\com\microej\example\wifi\setup\rest\Main.java)
2. Select **Run as -> Run Configuration** 
3. Select **MicroEJ Application** configuration kind
4. Click on **New launch configuration** icon
5. In **Execution** tab
	1. In **Target** frame, in **Platform** field, select a relevant platform (but not a virtual device)
	2. In **Execution** frame
		1. Select **Execute on Device**
		2. In **Settings** field, select **Build & Deploy**
6. Press **Apply**
7. Press **Run**
8. Copy the generated `.out` file path

### Flash
1. Use the appropriate flashing tool.

## Execution flow
1. Read the standard output of the board (this is described in the Platform documentation).
2. Power up the board
   * A Wi-Fi network is provided by the board with the SSID and password definied in Main (default values: MicroEJ_SoftAP, qwertyuiop).
   * On the standard output, the trace `Server started on [ip]:[port]` is printed.
3. Using a third party device, connect to the board's Wi-FI.
4. Use a Rest client to call the different endpoints.

## Depend on this project
Add the following line to your `module.ivy` or your `ivy.xml`:
> `<dependency org="com.microej.example.wifi" name="setup-rest" rev="+"/>`

# Build
1. Right click on the project
2. Click on `Build with EasyAnt`

# Requirements
  - EDC-1.2 or higher
  - BON-1.2 or higher
  - NET-1.0 or higher
  - ECOM-WIFI-2.1 or higher
  - ECOM-NETWORK-2.0 or higher

# Dependencies
This library depends the following project libraries:
   - com.microej.example.wifi.setup
   
To provide this, build the project or activate the ivy configuration `Resolve dependencies in workspace`.

_All dependencies are retrieved transitively by Ivy resolver_.

# Source
N/A

# Restrictions
None.


<!--
    Markdown
    Copyright 2018 IS2T. All rights reserved.
    This library is provided in source code for use, modification and test, subject to license terms.
    Any modification of the source code will break IS2T warranties on the whole library.
-->