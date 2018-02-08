
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/messageRecord

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/messageRecord" -d '
{
    "messageRecord": {
        "properties": {
            "id": {
                "type": "long"
            },
            "namespaceId": {
                "type": "integer"
            },
            "dstChannelToken": {
                "type": "string"
            },
            "dstChannelType": {
               "type": "string"
            },
            "status": {
                "type": "string"
            },
            "appId": {
                "type": "integer"
            },
            "messageSeq": {
                "type": "long"
            },
            "senderUid": {
                "type": "long"
            },
            "senderTag": {
                "type": "string"
            },
            "channelsInfo": {
                "type": "string"
            },
            "bodyType": {
                "type": "string"
            },
            "body": {
                "type": "string","index_analyzer":"standard_edge", "search_analyzer":"standard_edge"
            },
            "deliveryOption": {
                "type": "string"
            },
            "createTime": {
                "type": "date"
            },
            "deviceId": {
                "type": "string"
            },
            "indexId": {
                "type": "long"
            }
        }
    }
}
'
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~  
