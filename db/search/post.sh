ELASTIC=elasticsearch:9200
INDEX=everhomesv32x

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/topic

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/topic" -d '
{
	"topic": {
		"properties": {
			"subject":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
			"content":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "creatorNickName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "contentcategory":{"type":"string", "index":"not_analyzed"},
            "actioncategory":{"type":"string", "index":"not_analyzed"},
            "identify": {"type":"string", "index":"not_analyzed"},
			"appId":{"type":"long"},
			"forumId":{"type":"long"},
			"categoryId":{"type":"long"},
			"creatorUid":{"type":"long"},
            "visibilityScopeId":{"type":"long"},
            "visibilityScope":{"type":"long"},
            "visibleRegionType":{"type":"long"},
            "visibleRegionId":{"type":"long"},
            "embeddedId":{"type":"long"},
			"senderName":{"type":"string","index":"no", "store":"yes"},
			"senderAvatar":{"type":"string", "index":"no", "store":"yes"},
			"forumName":{"type":"string", "index":"no", "store":"yes"},
			"displayName":{"type":"string", "index":"no", "store":"yes"},
            "parentPostId": {"type":"long"},
			"namespaceId": {"type":"long"},
            "location": {
                "type": "geo_point",
                "geohash": true,
                "geohash_prefix": true,
                "geohash_precision": "50m"
            },
			"behaviorTime":{"type":"date"}
		}
	}
}
'
