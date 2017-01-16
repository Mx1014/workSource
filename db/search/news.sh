
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/news

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/news" -d '
{
    "news": {
        "properties": {
            "id": {
                "type": "long"
            },
            "namespaceId": {
                "type": "integer"
            },
            "ownerType": {
                "type": "string",
                "index": "not_analyzed"
            },
            "ownerId": {
                "type": "long"
            },
            "title": {
                "type": "string",
                "index_analyzer": "ansj_index",
                "search_analyzer": "ansj_query",
                "similarity": "BM25",
                "store": "yes"
            },
            "author": {
                "type": "string",
                "index": "not_analyzed"
            },
            "coverUri": {
                "type": "string",
                "index": "not_analyzed"
            },
            "contentType": {
                "type": "string",
                "index": "not_analyzed"
            },
            "content": {
                "type": "string",
                "index_analyzer": "ansj_index",
                "search_analyzer": "ansj_query",
                "similarity": "BM25",
                "store": "yes"
            },
            "contentAbstract": {
                "type": "string",
                "index": "not_analyzed"
            },
            "sourceDesc": {
                "type": "string",
                "index": "not_analyzed"
            },
            "sourceUrl": {
                "type": "string",
                "index": "not_analyzed"
            },
            "childCount": {
                "type": "long"
            },
            "forwardCount": {
                "type": "long"
            },
            "likeCount": {
                "type": "long"
            },
            "viewCount": {
                "type": "long"
            },
            "publishTime": {
                "type": "date"
            },
            "topIndex": {
                "type": "long"
            },
            "topFlag": {
                "type": "byte"
            },
            "status": {
                "type": "byte"
            },
            "creatorUid": {
                "type": "long"
            },
            "createTime": {
                "type": "date"
            }
        }
    }
}
'
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~                                                                                                                                                                                                                                                                             
~  
