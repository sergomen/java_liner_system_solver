#! /usr/bin/bash

pushd /home/georg/Dropbox/Programs/java_keep/die_fickene_zeit;

# save states
cp target/solver.jarstates ./
echo states saved;

# prepare
mvn clean:clean
>&2 echo hashes cleaned;

# test
mvn surefire:test
>&2 echo tests comlete;

# compile and place resources
mvn resources:resources
mvn compiler:compile
>&2 echo resources --done;
>&2 echo bytecode --done;

# assembly in solver.jar
mvn assembly:single
>&2 echo assembly --done;

# restore states
mv ./solver.jarstates target/solver.jarstates
>&2 echo states restored;

# run jar
>&2 echo jar --done;
java -jar target/solver.jar 2>&1 1>/dev/null

popd;