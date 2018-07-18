ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/equipmentStandardMap
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/equipmentStandardMap" -d '
 {
            "equipmentStandardMap": {
                "properties": {
                    "categoryId": {
                        "type": "long"
                    },
                    "equipmentName": {
                        "type": "string","index":"not_analyzed"
                    },
                    "equipmentNumber": {
                        "type": "string","index":"not_analyzed"
                    },
                    "inspectionCategoryId": {
                        "type": "long"
                    },
                    "namespaceId": {
                        "type": "long"
                    },
                    "ownerId": {
                        "type": "long"
                    },
                    "ownerType": {
                        "type": "string"
                    },
                    "repeatType": {
                        "type": "long"
                    },
                    "reviewResult": {
                        "type": "long"
                    },
                    "reviewStatus": {
                        "type": "long"
                    },
                    "standardName": {
                        "type": "string","index":"not_analyzed"
                    },
                    "standardNumber": {
                        "type": "string","index":"not_analyzed"
                    },
                    "status": {
                        "type": "long"
                    },
                    "targetId": {
                        "type": "long"
                    },
                    "targetType": {
                        "type": "string"
                    }
                }
            }
        }
'