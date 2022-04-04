#!/bin/bash
mvn install:install-file -Dfile=geode-core-9.5.1.jar -DgroupId=io.pivotal.gemfire -DartifactId=geode-core -Dversion=9.5.1 -Dpackaging=jar
mvn install:install-file -Dfile=shiro-core-1.4.0.jar -DpomFile=./shiro-core-1.4.0.pom
