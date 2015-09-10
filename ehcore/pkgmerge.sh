#!/bin/sh

rm -rf merge
mkdir merge
cd merge
OUT=ehcore-0.0.1-SNAPSHOT.war

tar -zxf ../output/ehcore.tar.gz
cd ehcore
tar -zxf ../../output/jar.tar.gz
jar -cvfM0 $OUT ./
mv $OUT ../../output
cd ../../
rm -rf merge
