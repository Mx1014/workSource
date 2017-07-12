ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/warehouseStock
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/warehouseStock" -d '
{
    "warehouseStock": {
        "properties": {
            "name":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "materialNumber":{"type":"string", "index":"not_analyzed"},
            "namespaceId":{"type":"integer"},
            "ownerId":{"type":"long"},
            "ownerType":{"type":"string", "index":"not_analyzed"},
            "warehouseId":{"type":"long"},
            "materialId":{"type":"long"},
            "status":{"type":"byte"},
            "warehouseStatus":{"type":"byte"},
            "updateTime":{"type":"date"},
            "categoryId":{"type":"long"}
        }
    }
}
'