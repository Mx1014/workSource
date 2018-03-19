ELASTIC=elasticsearch:9200
INDEX=everhomesv32x

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/paymentApplication
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/paymentApplication" -d '
{
    "paymentApplication": {
        "properties": {
            "title": {
                "type": "multi_field", 
                "analyzer":"simple",
                "fields": {
                    "title": {
                        "type": "string", 
                        "analyzer": "standard_edge"
                    }, 
                    "pinyin_gram": {
                        "type": "string", 
                        "analyzer": "pinyin_ngram_analyzer"
                    }, 
                    "pinyin_prefix": {
                        "type": "string", 
                        "analyzer": "pinyin_first_letter_analyzer"
                    }
                }
            },
            "contractName": {
                "type": "multi_field", 
                "analyzer":"simple",
                "fields": {
                    "contractName": {
                        "type": "string", 
                        "analyzer": "standard_edge"
                    }, 
                    "pinyin_gram": {
                        "type": "string", 
                        "analyzer": "pinyin_ngram_analyzer"
                    }, 
                    "pinyin_prefix": {
                        "type": "string", 
                        "analyzer": "pinyin_first_letter_analyzer"
                    }
                }
            },            
            "applicationNumber":{"type":"string","index":"no", "store":"yes"},
            "id": {"type":"long"},
            "communityId": {"type":"long"},
			"namespaceId": {"type":"integer"},
			"createTime":{"type":"date"},
			"paymentAmount":{"type":"double"},
            "status":{"type":"byte"}
            }
        }
}
'
