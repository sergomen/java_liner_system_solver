#! /usr/bin/bash

pushd /home/georg/Dropbox/Programs/java_keep/die_fickene_zeit;

# save states
cp target/solver.jarstates ./
echo states saved;

# try to install mvn libraries
mvn install;
>&2 echo install --complete;

# prepare
mvn clean:clean
>&2 echo hash clean --complete;

# test
mvn surefire:test
>&2 echo tests --comlete;

# compile and place resources
mvn resources:resources
>&2 echo resources --complete;
mvn compiler:compile
>&2 echo bytecode --complete;

# assembly in solver.jar
mvn assembly:single
>&2 echo assembly --complete;

# restore states
mv ./solver.jarstates target/solver.jarstates
>&2 echo states restored;

# run jar
>&2 echo jar --complete;
java -jar target/solver.jar 2>&1 1>/dev/null

popd;