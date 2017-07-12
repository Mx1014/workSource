ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/energyMeter
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/energyMeter" -d '
{
    "energyMeter": {
        "properties": {
            "name":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "meterNumber":{"type":"string", "index":"not_analyzed"},
            "communityId":{"type":"long"},
            "billCategoryId":{"type":"long"},
            "serviceCategoryId":{"type":"long"},
            "createTime":{"type":"long"},
            "status":{"type":"byte"},
            "meterType":{"type":"byte"}
        }
    }
}
'