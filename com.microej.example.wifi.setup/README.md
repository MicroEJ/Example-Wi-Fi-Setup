# Overview
This library defines an API that allows configuring headless Wi-Fi devices (no display, no input) by using the SoftAP mechanism. If needed it will stop and restart the SoftAP to join/scan.

# Usage
Add the following line to your `module.ivy` or your `ivy.xml`:
> `<dependency org="com.microej.example.wifi" name="setup" rev="+"/>`

- Extend or use SoftAPConnector to get the available AP and join one.
- ConfigurationManager is used to get the SoftAP configuration and get a pre-existing AP configuration.

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