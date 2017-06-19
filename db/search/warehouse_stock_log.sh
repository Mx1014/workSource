ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/warehouseStockLog
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/warehouseStockLog" -d '
{
    "warehouseStockLog": {
        "properties": {
            "materialName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "materialNumber":{"type":"string", "index":"not_analyzed"},
            "namespaceId":{"type":"integer"},
            "ownerId":{"type":"long"},
            "ownerType":{"type":"string", "index":"not_analyzed"},
            "warehouseId":{"type":"long"},
            "materialId":{"type":"long"},
            "requestType":{"type":"byte"},
            "createTime":{"type":"date"},
            "requestUid":{"type":"long"},
            "requestName":{"type":"string", "index":"not_analyzed"}
        }
    }
}
'