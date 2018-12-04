#!/usr/bin/env bash
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/enterpriseCustomer
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/enterpriseCustomer" -d '
{
	"enterpriseCustomer": {
		"properties": {
			"name":{
                "type":"string",
                "index":"not_analyzed",
                "fields": {
                    "raw": { 
                        "type":  "string",
                        "analyzer": "pinyin_first_letter_analyzer"
                    },
                    "baidu": {
                        "type": "string",
                        "index": "not_analyzed"
                    }
                }
            },
            "contactName":{"type":"string", "index":"not_analyzed"},
            "trackerName":{"type":"string", "index":"not_analyzed"},
            "customerContactName":{"type":"string", "index":"not_analyzed"},
            "contactMobile":{"type":"string", "index":"not_analyzed"},
            "contactAddress":{"type":"string", "index":"not_analyzed"},
            "contactMobile":{"type":"string", "index":"not_analyzed"},
            "trackingName":{"type":"string", "index":"not_analyzed"},
            "contentcategory":{"type":"string", "index":"not_analyzed"},
            "actioncategory":{"type":"string", "index":"not_analyzed"},
            "identify": {"type":"string", "index":"not_analyzed"},
            "id": {"type":"long"},
            "communityId": {"type":"long"},
			"namespaceId": {"type":"integer"},
			"categoryItemId": {"type":"long"},
			"levelItemId": {"type":"long"},
			"sourceItemId": {"type":"long"},
			"propertyType": {"type":"long"},
			"trackingUid": {"type":"long"},
            "status":{"type":"byte"},
            "propertyUnitPrice":{"type":"double"},
            "buildingId":{"type":"long"},
            "addressId":{"type":"long"},
            "adminFlag":{"type":"long"},
            "sourceId":{"type":"long"},
            "sourceType":{"type":"string","index":"not_analyzed"},
            "propertyArea":{"type":"double"},
			"lastTrackingTime":{"type":"date"},
			"createTime":{"type":"date"},
			"trackingTime":{"type":"date"},
			"requirementMinArea":{"type":"double"},
			"requirementMaxArea":{"type":"double"},
<<<<<<< HEAD
			"trackerName":{"type":"string", "index":"not_analyzed"},
			"entryInfo":{"type":"long"},
			"updateTime":{"type":"date"}

=======
			"trackerUid": {"type":"long"},
			"entryInfo":{"type":"long"}
>>>>>>> origin/5.11.0-fixmerge41304
		}
	}
}
'
