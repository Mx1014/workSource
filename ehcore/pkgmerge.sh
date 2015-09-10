#!/bin/sh

mkdir merge
cd merge
OUT=ehcore-0.0.1-SNAPSHOT.war

tar -zvf ../output/ehcore.tar.gz
cd ehcore
tar -zvf ../../output/jar.tar.gz
jar -cvfM0 $OUT ./
mv $OUT ../../output
cd ../../
