# Risk Game

[![Actions Status](https://github.com/i-reuts/TestRepo/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/i-reuts/csp-generator/actions)

A **Risk Game** implementation according to Warzone rules.

Generated documentation:
* [Latest Javadoc](https://i-reuts.github.io/RiskGame)
* [Latest Test Report](https://i-reuts.github.io/RiskGame/test/surefire-report.html)

## Setup, Configuration and Usage

#### Execution Requirements
* Java 15 Runtime Environment

#### Build Requirements
* Java 15 SDK
* `JAVA_HOME` environment variable set to the root Java directory. Example: `C:\Users\Username\.jdks\openjdk-15.0.1\`
* `JAVA_PATH` environment variable set to the bin Java directory. Example: `C:\Users\Username\.jdks\openjdk-15.0.1\bin\`

#### Maven Usage

The repository includes a Maven wrapper `mvnw` that can be used to execute all necessary Maven commands.

Some of the useful commands include:

Command | Description
------------ | -------------
`mvnw compile` | Compiles the project
`mvnw test` | Runs the unit tests
`mvnw package` | Creates a jar package for the application
`mvnw clean` | Clears the target directory
`mvnw surefire-report:report` | Generates an HTML test report
`mvnw javadoc:javadoc` | Generates Javadoc
