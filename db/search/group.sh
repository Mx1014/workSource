curl -XPUT 'http://localhost:9200/everhomesv3' -d '
    "settings" : {
        "number_of_shards" : 1
    },
'

curl -XDELETE http://localhost:9200/everhomesv3/_mapping/group

curl -XPUT 'http://localhost:9200/everhomesv3/_mapping/group' -d '
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
            "createTime":{"type":"date"}
        }
    }
}
'

echo "done"
