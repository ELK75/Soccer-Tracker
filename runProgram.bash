#!/bin/bash
# Bash file to compile and run file
compile="javac -cp 'sassy_user.jar;.' Main.java"
eval $compile
run="java -cp 'sassy_user.jar;.' Main"
eval $run