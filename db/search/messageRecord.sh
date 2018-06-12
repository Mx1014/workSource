
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/messageRecord

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/messageRecord" -d '
{
    "messageRecord": {
        "properties": {
            "id": {
                "type": "long",
                "index": "not_analyzed"
            },
            "namespaceId": {
                "type": "integer",
                "index": "not_analyzed"
            },
            "dstChannelToken": {
                "type": "string"
            },
            "dstChannelType": {
               "type": "string"
            },
            "status": {
                "type": "string",
                "index": "not_analyzed"
            },
            "appId": {
                "type": "integer"
            },
            "messageSeq": {
                "type": "long",
                "index": "not_analyzed"
            },
            "senderUid": {
                "type": "long",
                "index": "not_analyzed"
            },
            "senderTag": {
                "type": "string",
                "index": "not_analyzed"
            },
            "channelsInfo": {
                "type": "string",
                "index": "not_analyzed"
            },
            "bodyType": {
                "type": "string",
                "index": "not_analyzed"
            },
            "body": {
                "type": "string","index_analyzer":"standard_edge", "search_analyzer":"standard_edge"
            },
            "deliveryOption": {
                "type": "string",
                "index": "not_analyzed"
            },
            "createTime": {
                "type": "date"
            },
            "deviceId": {
                "type": "string",
                "index": "not_analyzed"
            },
            "indexId": {
                "type": "long",
                "index": "not_analyzed"
            }
        }
    }
}
'
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~  
