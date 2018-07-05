ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/enterpriseCustomer
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/enterpriseCustomer" -d '
{
	"enterpriseCustomer": {
		"properties": {
			"name":{
                "type":"string",
                "index_analyzer":"ansj_index", 
                "search_analyzer":"ansj_query", 
                "similarity":"BM25", 
                "store":"yes",
                "fields": {
                    "raw": { 
                        "type":  "string",
                        "analyzer": "pinyin_first_letter_analyzer"
                    }
                }
            },
			"contactName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "contactAddress":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "contactMobile":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "trackingName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "contentcategory":{"type":"string", "index":"not_analyzed"},
            "actioncategory":{"type":"string", "index":"not_analyzed"},
            "identify": {"type":"string", "index":"not_analyzed"},
            "id": {"type":"long"},
            "communityId": {"type":"long"},
			"namespaceId": {"type":"integer"},
			"categoryItemId": {"type":"long"},
			"levelItemId": {"type":"long"},
			"trackingUid": {"type":"long"},
			"sourceItemId": {"type":"long"},
			"propertyType": {"type":"long"},
			"trackingUid": {"type":"long"},
            "status":{"type":"byte"},
            "propertyUnitPrice":{"type":"double"},
            "buildingId":{"type":"string"},
            "addressId":{"type":"string"},
            "adminFlag":{"type":"long"},
            "sourceId":{"type":"long"},
            "sourceType":{"type":"string"},
            "propertyArea":{"type":"double"},
			"lastTrackingTime":{"type":"date"}
		}
	}

}
'
