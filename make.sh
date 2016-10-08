#! /usr/bin/bash

pushd /home/georg/Dropbox/Programs/java_keep/die_fickene_zeit;

# save states
cp target/solver.jarrowStates ./
echo states saved;

# try to install mvn libraries
mvn install;
>&2 echo install --complete;

# prepare
mvn clean:clean
>&2 echo hash clean --complete;

# compile and place resources
mvn resources:resources
>&2 echo resources --complete;
mvn compiler:compile
>&2 echo bytecode --complete;

# test
mvn test
>&2 echo tests --comlete;

# assembly in solver.jar
mvn assembly:single
>&2 echo assembly --complete;

# restore states
mv ./solver.jarrowStates target/solver.jarrowStates
>&2 echo states restored;

# run jar
>&2 echo jar --complete;
java -jar target/solver.jar 2>&1 1>/dev/null

popd;