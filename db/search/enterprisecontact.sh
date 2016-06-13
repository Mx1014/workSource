ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/enterprisecontact

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/enterprisecontact" -d '
{
    "enterprisecontact" : {
        "properties" : {
            "userName": {
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
            "department":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
			"contact":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
			"userId":{"type":"long"},
			"enterpriseId":{"type":"long"}
        }
    }
}
'

echo "done"