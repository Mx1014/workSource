curl -XGET http://127.0.0.1:9200/everhomesv3/group/_search?pretty=True -d '
{
    "query": {
        "prefix": {
            "category": "/生活/真美丽"
        }
    }
}
'
