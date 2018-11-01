ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/energyTask
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/energyTask" -d '
{
    "energyTask": {
        "properties": {         
           "communityId": {
                "type": "long"
            },
            "endTime": {
                "type": "date",
                "format": "dateOptionalTime"
            },
            "meterName": {
                "type": "string",
                "index":"not_analyzed"
            },
            "namespaceId": {
                "type": "long"
            },
            "planId": {
                "type": "long"
            },
            "startTime": {
                "type": "date",
                "format": "dateOptionalTime"
            },
            "status": {
                "type": "long"
            }
        }
    }
}
'