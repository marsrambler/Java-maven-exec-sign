#!/bin/bash

echo "install local jar(s)..."

mvn install:install-file -Dfile=commons-codec-1.3.jar -DgroupId=com.jacktest -DartifactId=commons-codec -Dversion=1.3 -Dpackaging=jar -DgeneratePom=true