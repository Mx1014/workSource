ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/pmowner
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/pmowner" -d '
{
    "pmowner": {
        "properties": {
            "contactToken":{"type":"string","index":"not_analyzed"},
            "contactName":{"type":"string","index":"not_analyzed"},
            "contactExtraTels":{"type":"string","index":"not_analyzed"},
            "communityId":{"type":"string","index": "not_analyzed"},
            "createTime":{"type":"date"},
            "id":{"type":"long"},
            "orgOwnerTypeId":{"type":"integer"}
        }
    }
}
'