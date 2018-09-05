ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/warehouse
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/warehouse" -d '
{
    "warehouse": {
        "properties": {
        	"communityId":{"type":"long"},
            "name":{"type":"string", "index":"not_analyzed"},
            "ownerId":{"type":"long"},
            "ownerType":{"type":"string", "index":"not_analyzed"},
            "namespaceId":{"type":"integer"},
            "status":{"type":"byte"}
        }
    }
}
'