ELASTIC=elasticsearch:9200
INDEX=everhomesv3
curl -XDELETE http://$ELASTIC/$INDEX/_mapping/enterprise

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/enterprise" -d '
{
    "enterprise" : {
	"_all": { "analyzer": "whitespace" },
        "properties" : {
	    "id":{"type":"long"},
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
            "description":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
	    "namespaceId":{"type":"long"},
            "createTime":{"type":"date"},
	    "communityId":{"type":"long"},
	    "addresses":{"type":"string", "analyzer": "standard_edge"},
	    "buildings":{"type":"string", "index": "not_analyzed"},
	    "setAdminFlag":{"type":"integer"},
	    "organizationType":{"type":"string", "index":"not_analyzed"}
        }
    }
}
'
