ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/hottag
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/hottag" -d '
{
    "hottag": {
        "properties": { 
            "name":{"type":"string","analyzer":"standard"},
            "serviceType": {
                "type": "string",
                "index": "not_analyzed"
                },
            "hotFlag": {
                "type": "byte",
                "index": "not_analyzed"
                }
        }
    }
}
'
