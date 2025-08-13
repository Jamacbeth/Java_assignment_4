#!/bin/bash
cd "$(dirname "$0")"   # Go to the folder where this script is located
java -jar "LanguageSuggestion.jar"
read -p "Press any key to close..."


