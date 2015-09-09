#!/bin/sh

mkdir merge
cd merge

tar -zvf ../output/ehcore.tar.gz
cd ehcore
tar -zvf ../../output/jar.tar.gz
jar -cvfM0 ehcore.war ./
mv ehcore.war ../../output
cd ../../
