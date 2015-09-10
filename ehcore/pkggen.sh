#!/bin/sh

rm -rf target/genpkg
rm -rf output
mkdir output
mkdir -p target/genpkg/ehcore
cd target/genpkg/ehcore

war=`ls ../../*.war|sed -n 1p`
jar -xf $war

find . -name "*.jar" > z.list
cat z.list|xargs -i md5sum {}|sort >> checksumjars
echo "expected b6081ee01739846ec864d345179f7519"
md5sum checksumjars
tar -T z.list -czf jar.tar.gz
sleep 1

rm -f z.list
mv checksumjars ../../../output
rm -rf static/apidocs
find . -name "*.jar"|xargs rm -f
mv jar.tar.gz ../../../output

cd ..
tar -zcf ehcore.tar.gz ./ehcore
mv ehcore.tar.gz ../../output

cd ../../
rm -rf merge
