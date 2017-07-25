http://10.1.10.37:9200/_mapping/uniongroupDetails
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/uniongroupDetails

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/uniongroupDetails" -d '
{
    "uniongroupDetails": {
        "properties": {
            "id": {
                "type": "long"
            },
            "namespaceId": {
                "type": "integer"
            },
            "groupType": {
                "type": "string"
            },
            "groupId": {
                "type": "long"
            },
            "detailId": {
                "type": "long"
            },
            "targetType": {
                "type": "string"
            },
            "targetId": {
                "type": "long"
            },
            "enterpriseId": {
                "type": "long"
            },
            "contact_name": {
                "type": "string","index_analyzer":"standard_edge", "search_analyzer":"standard_edge"
            },
            "contact_token": {
                "type": "string"
            },
            "update_time": {
                "type": "date"
            },
            "operator_uid": {
                "type": "long"
            },
            "department": {
                "properties": {
                    "department": {"type":"multi_field",
                    "fields" : {
                        "department_id" : {"type":"long"},
                        "department_name" : {"type":"string"}
                        }
                    }
                }
            },
            "job_position": {
                "properties": {
                    "job_position": {"type":"multi_field",
                    "fields" : {
                        "job_position_id" : {"type":"long"},
                        "job_position_name" : {"type":"string"}
                        }
                    }
                }
            }
        }
    }
}
'
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~  
