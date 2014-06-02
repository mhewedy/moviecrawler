#!/bin/bash

mvn clean test assembly:single

(echo '#!/usr/bin/java -jar'; cat target/*.jar) > target/moviecrawler; chmod +x target/moviecrawler

cp -v target/moviecrawler ~/bin/
