ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/contract
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/contract" -d '
{
	"contract": {
		"properties": {
			"name":{ "type":"string","index":"not_analyzed"},
			"customerName":{"type":"string","index":"not_analyzed"},
            "contractNumber":{
                "type":"string", 
                "index":"not_analyzed",
                "fields": {
                    "raw": { 
                        "type":  "string",
                        "analyzer": "pinyin_first_letter_analyzer"
                    }
                }},
            "identify": {"type":"string", "index":"not_analyzed"},
            "id": {"type":"long"},
            "communityId": {"type":"long"},
			"namespaceId": {"type":"integer"},
            "contractType":{"type":"byte"},
            "status":{"type":"byte"},
            "paymentFlag":{"type":"byte"},
            "categoryItemId":{"type":"long"},
            "customerType":{"type":"byte"},
			"contractStartDate":{"type":"date"},
			"contractEndDate":{"type":"date"},
			"updateTime":{"type":"date"},
			"categoryId":{"type":"long"},
			"rent":{"type":"double"}
		}
	}

}
'