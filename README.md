# ThesisWork

The content of the repository:

traditional-example : the traditional implementation of proxy (merger) uService
reactive-example : the reactive implementation of proxy (merger) uService
base-services
 - item-service: implementation of item uService (traditional)
 - manufacturer-service: implementation of manufacturer uService (traditional)
 - discovery-service
others : learning materials
MongoDB Collections: sample database
JmeterTests: test files for sytem (jmeter)

simple description how to use the system

scenario I traditional example
1. start the itemService
2. start the manufacturerService
3. start the traditional-example

scenario I reactive example
1. start the itemService
2. start the manufacturerService
3. start the reactive-example

Mongo db:
version: 
how to setup


compilation:
in root folder
```shell
mvn clean compile
```