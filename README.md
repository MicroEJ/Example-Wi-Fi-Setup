# Overview
This repository contains the projects to setup a Wi-Fi connection using the SoftAP mode a Wi-Fi chip.

# Usage
Setup ivy to resolve its dependencies in workspace: 
 1. Click on **Window -> Preferences**
 2. Go to **Ivy -> Classpath Container**
 3. Check **Resolve dependencies in Workspace**
 4. Press **Apply and Close**

Import the projects:
 * [com.microej.example.wifi.setup](com.microej.example.wifi.setup): this project provides abstraction classes to setup the Wi-Fi AP credential using a Soft AP.
 * [com.microej.example.wifi.setup.rest](com.microej.example.wifi.setup.rest): this project mounts a rest server to provide the Access Point credential to join a Wi-Fi.
 * [com.microej.example.wifi.setup.web](com.microej.example.wifi.setup.web): this project presents a HTTP page in addition of a rest server to provide the Access Point credential to join a Wi-Fi.
 
 Both `com.microej.example.wifi.setup.rest` and `com.microej.example.wifi.setup.web` contain an Entry point to execute the project.
 The full demo will be using `com.microej.example.wifi.setup.web`, how to run it is explained in the project's [README.md](com.microej.example.wifi.setup.web/README.md).
 

# Requirements
  - EDC-1.2 or higher
  - SSL-2.1 or higher
  - NET-1.0 or higher
  - ECOM-WIFI-2.1 or higher
  - ECOM-NETWORK-2.0 or higher

# Dependencies
 _All dependencies are retrieved transitively by Ivy resolver_.

# Source
N/A

# Restrictions
None.



---  
_Markdown_   
_Copyright 2018-2022 MicroEJ Corp. All rights reserved._   
_Use of this source code is governed by a BSD-style license that can be found with this software._