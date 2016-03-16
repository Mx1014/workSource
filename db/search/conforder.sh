ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/conforder

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/conforder" -d '
{
    "conforder" : {
        "properties" : {
            "enterpriseName": {
                "type": "multi_field", 
                "analyzer":"simple",
                "fields": {
                    "name": {
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
			"enterpriseId":{"type":"long"}
        }
    }
}
'

echo "done"