#!/bin/sh

content_base=/data1/ehserver/server/content
data_base=/data1/database/content
if [ -z ${data_base}/dbs/beansdb1 ]; then
    mkdir -p ${data_base}/dbs/beansdb1
fi

if [ -z ${data_base}/dbs/beansdb2 ]; then
    mkdir -p ${data_base}/dbs/beansdb2
fi

${content_base}/bin/beansdb -p 7900 -d -P ${content_base}/beansdb1.pid -H ${data_base}/dbs/beansdb1
#echo ${content_base}/bin/beansdb -p 7900 -d -P ${content_base}/beansdb1.pid -H ${data_base}/dbs/beansdb1
${content_base}/bin/beansdb -p 7901 -d -P ${content_base}/beansdb2.pid -H ${data_base}/dbs/beansdb2
nohup ${content_base}/bin/proxy -conf ${content_base}/conf/proxy.yaml -basepath ${content_base}  > ${content_base}/logs/proxy 2>&1 &
nohup ${content_base}/server/contentserver --config=${content_base}/server/conf/config.ini > ${content_base}/logs/contentserver.log 2>&1 &
#cd ${content_base}/server/ && nohup ./contentserver > contentserver.log 2>&1 &

