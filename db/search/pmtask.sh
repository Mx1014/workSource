ELASTIC=elasticsearch:9200
INDEX=everhomesv32x

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/pmtask
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/pmtask" -d '

{
      "pmtask" : {
        "properties" : {
          "address" : {
            "type" : "string"
          },
          "categoryId" : {
            "type" : "long",
            "index": "not_analyzed"
          },
          "content" : {
            "type" : "string"
          },
          "createTime" : {
            "type" : "long"
          },
          "creatorUid" : {
            "type" : "long",
            "index": "not_analyzed"
          },
          "mobile" : {
            "type" : "string"
          },
          "namespaceId" : {
            "type" : "long"
          },
          "nickName" : {
            "type" : "string"
          },
          "ownerId" : {
            "type" : "long",
            "index": "not_analyzed"
          },
          "ownerType" : {
            "type" : "string"
          },
          "status" : {
            "type" : "long"
          }
        }
      }
}
'
