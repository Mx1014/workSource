ELASTIC=elasticsearch:9200
INDEX=everhomesv32x

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/community
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/community" -d '
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
            "cityName":{"type":"string","index":"no", "store":"yes"},
            "cityId": {
                "type": "long",
                "index": "not_analyzed"
                },
            "regionId": {
                "type": "long",
                "index": "not_analyzed"
                },
            "communityType": {
                "type": "long",
                "index": "not_analyzed"
                }
            }
        }
}
'
