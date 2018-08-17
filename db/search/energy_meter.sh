ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/energyMeter
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/energyMeter" -d '
{
    "energyMeter": {
        "properties": {
            "addressId" : {
            "type" : "string"
          },
          "assignFlag" : {
            "type" : "long"
          },
          "assignPlan" : {
            "type" : "string"
          },
          "billCategoryId" : {
            "type" : "long"
          },
          "buildingId" : {
            "type" : "string"
          },
          "communityId" : {
            "type" : "long"
          },
          "createTime" : {
            "type" : "long"
          },
          "meterNumber":{
                "type":"string", 
                "index":"not_analyzed",
                "fields": {
                    "raw": { 
                        "type":  "string",
                        "analyzer": "pinyin_first_letter_analyzer"
                    }
           }},
          "meterType" : {
            "type" : "long"
          },
          "name" : {
            "type" : "string",
            "index":"not_analyzed"
          },
          "operatorName" : {
            "type" : "string"
          },
          "serviceCategoryId" : {
            "type" : "long"
          },
          "status" : {
            "type" : "long"
          }
        }
    }
}
'