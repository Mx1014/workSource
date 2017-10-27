
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/news

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/news" -d '
{
     "news" : {
            "properties" : {
              "author" : {
                "type" : "string"
              },
              "categoryId" : {
                "type" : "long"
              },
              "childCount" : {
                "type" : "long"
              },
              "communityIds" : {
                "type" : "long"
              },
              "content" : {
                "type" : "string"
              },
              "contentAbstract" : {
                "type" : "string"
              },
              "contentType" : {
                "type" : "string"
              },
              "coverUri" : {
                "type" : "string"
              },
              "createTime" : {
                "type" : "long"
              },
              "creatorUid" : {
                "type" : "long"
              },
              "deleteTime" : {
                "type" : "long"
              },
              "deleterUid" : {
                "type" : "long"
              },
              "description" : {
                "type" : "string"
              },
              "forwardCount" : {
                "type" : "long"
              },
              "id" : {
                "type" : "long"
              },
              "likeCount" : {
                "type" : "long"
              },
              "namespaceId" : {
                "type" : "long"
              },
              "ownerId" : {
                "type" : "long"
              },
              "ownerType" : {
                "type" : "string"
              },
              "phone" : {
                "type" : "string"
              },
              "publishTime" : {
                "type" : "long"
              },
              "sourceDesc" : {
                "type" : "string"
              },
              "sourceUrl" : {
                "type" : "string"
              },
              "status" : {
                "type" : "long"
              },
              "tag" : {
                "type" : "string",
                "index" : "not_analyzed"
              },
              "title" : {
                "type" : "string"
              },
              "topFlag" : {
                "type" : "long"
              },
              "topIndex" : {
                "type" : "long"
              },
              "viewCount" : {
                "type" : "long"
              },
              "visibleType" : {
                "type" : "string"
              }
            }
          }

}
'
