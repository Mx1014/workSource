ELASTIC=127.0.0.1:9200
#curl -XGET http://$ELASTIC/everhomesv3/group/_search?pretty=True -d '
#{
#    "query": {
#        "prefix": {
#            "category": "/生活/真美丽"
#        }
#    }
#}
#'

#curl -XGET http://$ELASTIC/everhomesv3/community/_search?pretty=True&q=*

curl -XGET http://$ELASTIC/everhomesv3/community/_search?pretty=True -d '
{
  "from" : 1,
  "size" : 20,
  "query" : {
    "filtered" : {
      "query" : {
        "multi_match" : {
          "query" : "通苑",
          "fields" : [ "name^5.0", "name.pinyin_prefix^2.0", "name.pinyin_gram^1.0" ],
          "analyzer" : "simple"
        }
      },
      "filter" : {
        "term" : {
          "cityId" : 5636106
        }
      }
    }
  }
}
'
