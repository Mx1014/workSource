ELASTIC=127.0.0.1:9200
curl -XGET http://$ELASTIC/everhomesv3/group/_search?pretty=True -d '
{
    "query": {
        "prefix": {
            "category": "/生活/真美丽"
        }
    }
}
'

curl -XGET http://$ELASTIC/everhomesv3/community/_search?pretty=True&q=*
