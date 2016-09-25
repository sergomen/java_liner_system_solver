#! /usr/bin/bash

# save states
cp target/solver.jarstates ./

# prepare
mvn clean:clean

# test
mvn surefire:test

# compile and place resources
mvn resources:resources
mvn compiler:compile

# assembly in solver.jar
mvn assembly:single

# restore states
mv ./solver.jarstates target/solver.jarstates

# run jar
java -jar target/solver.jar

