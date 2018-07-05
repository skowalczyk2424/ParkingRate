#!/bin/bash
nohup java -jar ../target/task-0.0.1-SNAPSHOT.jar > ../scripts/temp/logs.txt 2>&1 &
echo $! > ../scripts/temp/pid.file


