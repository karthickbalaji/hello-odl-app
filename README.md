# Building your first Opendaylight application

A simple application that demonstrates and help understand key ODL concepts for beginners

Concepts covered - 

-	Yang Data Modelling, RPC, notifications
-	yangtools Code generation and build
-	Simple ODL RPC implementation
-	Simple MD-SAL datastore read and write operations/transactions
-	MD-SAL Data Tree change events Listener/Processing
-	ODL notifications Publisher/Listener

Overview
---------

We build a very simple network of whose JSON data model is as simple as below
```json
  "network": {
    "nodes": [
      {
        "name": "hello",
        "location": "world"
      }
    ]
  }
```
- We define this model in yang
- We define couple RPC methods to create network, update location of specific node
- We define a locationChanged notification, to be notified whenever there is a location change
- We listen for MD SAL Data Tree changes by registering to ODL DataTreeChangeListener, log changes to console
- DataTreeChangeListener checks for location and if changed will publish locationChanged
- Registered Notification listener will listen and log to console of event reception

Pre-requisites
---------------

- Java 11 compliant JDK
- Apache Maven 3.5.2 or later

Opendaylight has its own nexus repo, it is suggested to set your maven pointing to it
>cp -n ~/.m2/settings.xml{,.orig} ; wget -q -O - https://raw.githubusercontent.com/opendaylight/odlparent/master/settings.xml > ~/.m2/settings.xml


How to Build 
--------------

At the root folder - run below maven command

>mvn clean install -DskipTests

Note -Dcheckstyle.skip is recommended to disable validations
For some reason this did not work for me, so i disabled the plugin, basically you dont need this now

How to Run
------------

After successful build, you would have artifacts generated under karaf/ folder

cd to karaf/target/assembly/bin and run ./karaf to start karaf console

>cd /karaf/target/assembly/bin

>./karaf

On Karaf console - check for logs using below command to check for your application correctly started

opendaylight-user@root>log:display | grep Sample
19:26:35.540 INFO [Blueprint Extender: 2] SampleNetworkProvider Session Initiated
19:26:36.030 INFO [Blueprint Extender: 3] SampleNetworkCliCommandImpl initialized

How to Test
-----------
Keep a watch on console log
>opendaylight-user@root>log:tail 

Create a network using below REST API (use your favourite REST console)

POST http://localhost:8181/restconf/config/samplenetwork:create-network/
```json
{
  "input": {
    "net": {
      "devices": [
        {
          "name": "Cisco",
          "location": "lab1"
        },
        {
          "name": "Alcatel",
          "location": "lab2"
        }
      ]
    }
  }
}
```
Change location of a device using below API, watch for logs

PUT http://localhost:8181/restconf/config/samplenetwork:network/nodes/Cisco
```json
{
  "nodes": [
    {
      "name": "Cisco",
      "location": "home"
    }
  ]
}
```
