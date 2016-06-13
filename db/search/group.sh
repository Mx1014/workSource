ELASTIC=elasticsearch:9200
INDEX=everhomesv32x

curl -XPUT "http://$ELASTIC/$INDEX" -d '
    "settings" : {
        "number_of_shards" : 1
    },
'

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/group

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/group" -d '
{
    "group" : {
        "properties" : {
            "name":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "description":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
			"tags": {
				"properties": {
					"tag": {"type":"multi_field",
	                "fields" : {
	                    "name" : {"type":"string", "index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25"},
	                    "untouched" : {"type":"string", "index":"not_analyzed"}
	                	}
	            	}
				}
			},
            "category": {"type":"string", "index":"not_analyzed"},
            "creatorUid":{"type":"long"},
            "categoryId":{"type":"long"},
			"namespaceId":{"type":"long"},
            "createTime":{"type":"date"}
        }
    }
}
'

echo "done"
