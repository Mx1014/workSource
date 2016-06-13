ELASTIC=10.1.1.218:9200
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
  "from" : 0,
  "size" : 20,
  "query" : {
    "filtered" : {
      "query" : {
        "multi_match" : {
          "query" : "hy",
          "fields" : [ "name^5.0", "name.pinyin_prefix^2.0", "name.pinyin_gram^1.0" ]
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

#http://elasticsearch:9200/everhomesv3/community/_search?q=ccxq
#http://elasticsesarch:9200/everhomesv3/_analyze?text=翠城小区&analyzer=pinyin_first_letter&pretty
#http://elasticsearch:9200/everhomesv3/_analyze?text=%E7%BF%A0%E5%9F%8E%E5%B0%8F%E5%8C%BA&analyzer=standard_edge&pretty
#curl -H "Content-Type: text/html; charset=UTF-8" -XGET http://$ELASTIC/everhomesv3/_analyze?text=%E7%BF%A0%E5%9F%8E%E5%B0%8F%E5%8C%BA&analyzer=standard_edge&pretty
#http://elasticsearch:9200/everhomesv3/community/_mapping?pretty
#curl -XPOST http://$ELASTIC/everhomesv3/_refresh

curl -XGET http://$ELASTIC/everhomesv3/community/_search?pretty=True -d '{
  "from" : 0,
  "size" : 20,
  "query" : {
      "multi_match" : {
        "query" : "ys",
        "fields" : ["name.pinyin_prefix"]
      }
  }
}'
