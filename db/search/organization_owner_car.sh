ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/organizationOwnerCar
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/organizationOwnerCar" -d '
{
    "organizationOwnerCar": {
        "properties": {
            "plateNumber":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "contacts":{"type":"string","index_analyzer":"ansj_index", "search_analyzer":"ansj_query", "similarity":"BM25", "store":"yes"},
            "communityId":{"type":"long"},
            "parkingType":{"type":"integer"}
        }
    }
}
'