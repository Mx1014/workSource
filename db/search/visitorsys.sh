
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/visitorsys

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/visitorsys" -d '
{
     "visitorsys" : {
            "properties" : {
            	"namespaceId" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "ownerType" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "ownerId" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "id" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "visitorName" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "followUpNumbers" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "visitorPhone" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "visitReasonId" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "visitReason" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "inviterId" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "inviterName" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "plannedVisitTime" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "createTime" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "visitTime" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "visitStatus" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "bookingStatus" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "visitorType" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "enterpriseId" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "enterpriseName" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "officeLocationId" : {
                    "type": "long",
                    "index": "not_analyzed"
                },
                "officeLocationName" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
               "statsDate" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "statsHour" : {
                    "type": "string",
                    "index": "not_analyzed"
                 },
                 "statsWeek" : {
                    "type": "string",
                    "index": "not_analyzed"
                 },
                 "idNumber" : {
                     "type": "string",
                     "index": "not_analyzed"
                  }

            }
          }

}
'
