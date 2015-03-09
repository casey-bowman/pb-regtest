Alternative Personal BTC Project
================================

pb-regtest
----------
Module for playing with bitcoin in regtest mode using spock


Prerequisites
-------------
- JDK 6 or higher
- [Bitcoin Core 0.10.0](https://bitcoin.org/en/download) or higher 



Creating an IDEA project
---------------------------
Type: ./gradlew cleanIdea idea

Open the generated project in IDEA. You should now be able to build the project, and to run the specs like you would run a JUnit test.

Adjust the value for btc.path in local.properties if necessary. 


Running tests from the command line
-----------------------------------

Type: ./gradlew cleanTest test

Open build/reports/tests/index.html to check the test results.

Adjust the value for btc.path in local.properties if necessary.
