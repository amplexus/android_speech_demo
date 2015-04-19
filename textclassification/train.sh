#!/bin/bash

# From http://opennlp.apache.org/documentation/1.5.2-incubating/manual/opennlp.html#tools.doccat

APACHENLP_HOME=/home/craig/apache-opennlp-1.5.3

# TRAIN: Reads trainer.txt and writes to trainer.bin
${APACHENLP_HOME}/bin/opennlp DoccatTrainer -lang en -data ./trainer.txt -model ./trainer.bin
mv trainer.bin ../app/src/main/res/raw/

# TEST: Input file most contain one "document" at a time...
#${APACHENLP_HOME}/bin/opennlp Doccat trainer.bin < realdata.txt
