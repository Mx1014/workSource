ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/pmtask
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/pmtask" -d '

{
      "pmtask" : {
        "properties" : {
          "address" : {
            "type" : "string"
          },
          "addressId" : {
            "type" : "long"
          },
          "buildingName" : {
            "type" : "string",
			"index": "not_analyzed"
          },
          "content" : {
            "type" : "string"
          },
          "createTime" : {
            "type" : "long"
          },
          "creatorUid" : {
            "type" : "long"
          },
          "flowCaseId" : {
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
          "requestorName" : {
            "type" : "string"
          },
          "requestorPhone" : {
            "type" : "string"
          },
          "status" : {
            "type" : "long"
          },
          "taskCategoryId" : {
            "type" : "long"
          },
          "organizationUid":{
             "type" : "long",
             "null_value": 0
          },
          "star" : {
              "type" : "String"
          },
          "amount" : {
              "type" : "long",
              "null_value": 0
          },
          "appId" : {
                "type" : "long"
            }
        }
      }
}
'
