ELASTIC=elasticsearch:9200

curl -XDELETE http://$ELASTIC/everhomesv3/_mapping/community
curl -XPUT "http://$ELASTIC/everhomesv3/_mapping/community" -d '
{
    "community": {
        "properties": {
            "name": {
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
            "cityId": {
                "type": "long",
                "index": "not_analyzed"
                }
            }
        }
}
'
