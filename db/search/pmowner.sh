ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/pmowner
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/pmowner" -d '
{
    "pmowner": {
        "properties": {
            "contactToken":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "contactName":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "communityId":{"type":"long"},
            "orgOwnerTypeId":{"type":"integer"}
        }
    }
}
'