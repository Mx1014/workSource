#!/bin/sh

mkdir merge
cd merge

tar -zxvf ../output/ehcore.tar.gz
cd ehcore
tar -zxvf ../../output/jar.tar.gz
jar -cvfM0 ehcore.war ./
mv ehcore.war ../../output
cd ../../
