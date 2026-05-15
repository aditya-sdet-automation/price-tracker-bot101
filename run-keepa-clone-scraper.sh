#!/bin/bash

#Defining absolute paths
JAR_PATH="/home/adi_swordsmage/IdeaProjects/PriceScraperAutomation/target/PriceScraperAutomation-1.0-SNAPSHOT-jar-with-dependencies.jar"
LOG_PATH="/home/adi_swordsmage/IdeaProjects/PriceScraperAutomation/LogFile.txt"

#cd
cd /home/adi_swordsmage/IdeaProjects/PriceScraperAutomation/

#excecute java, append stdout to log and stderror to stdout
java -jar "$JAR_PATH" >> "$LOG_PATH" 2>&1
