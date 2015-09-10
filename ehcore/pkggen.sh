#!/bin/sh

rm -rf target/genpkg
rm -rf output
mkdir output
mkdir -p target/genpkg/ehcore
cd target/genpkg/ehcore

war=`ls ../../*.war|sed -n 1p`
jar -xf $war

find . -name "*.jar"|grep -v "./WEB-INF/lib/eh"|sort > z.list
cat z.list|xargs -i md5sum {}|sort >> checksumjars
echo "5458104ed4981a9722f58f439a988eaf  expected"
md5sum checksumjars
tar -T z.list -czf jar.tar.gz

mv checksumjars ../../../output
rm -rf static/apidocs
cat z.list|xargs rm -f
rm -f z.list
mv jar.tar.gz ../../../output

cd ..
tar -zcf ehcore.tar.gz ./ehcore
mv ehcore.tar.gz ../../output

cd ../../
rm -rf merge
