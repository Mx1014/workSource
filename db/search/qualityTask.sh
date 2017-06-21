ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/qualityTask
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/qualityTask" -d '
{
    "energyMeter": {
        "properties": {
            "targetName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "executorName":{"type":"string","index": "not_analyzed"},
            "operatorName":{"type":"string","index": "not_analyzed"},
            "namespaceId":{"type":"integer"},
            "parentId":{"type":"long"},
            "sampleId":{"type":"long"},
            "ownerId":{"type":"long"},
            "ownerType":{"type":"string","index": "not_analyzed"},
            "targetId":{"type":"long"},
            "manualFlag":{"type":"byte"},
            "executorId":{"type":"long"},
            "operatorId":{"type":"long"},
            "startDate":{"type":"date"},
            "endDate":{"type":"date"},
            "status":{"type":"byte"}
            "reviewResult":{"type":"byte"}
        }
    }
}
'

            
