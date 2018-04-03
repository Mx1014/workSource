ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/energyMeterReadingLog
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/energyMeterReadingLog" -d '
{
    "energyMeterReadingLog": {
        "properties": {
            "meterName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "meterNumber":{"type":"string", "index":"not_analyzed"},
            "operatorName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "communityId":{"type":"long"},
            "billCategoryId":{"type":"long"},
            "serviceCategoryId":{"type":"long"},
            "operateTime":{"type":"long"},
            "reading":{"type":"double"},
            "meterType":{"type":"byte"},
            "changeFlag":{"type":"byte"},
            "resetFlag":{"type":"byte"}
        }
    }
}
'