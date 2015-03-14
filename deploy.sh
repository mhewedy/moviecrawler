#!/bin/bash

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_11.jdk/Contents/Home

mvn clean package assembly:single

(echo '#!/usr/bin/java -jar'; cat target/*.jar) > target/moviecrawler; chmod +x target/moviecrawler
#cat target/*.jar > target/moviecrawler; chmod +x target/moviecrawler

cp -v target/moviecrawler ~/bin/
