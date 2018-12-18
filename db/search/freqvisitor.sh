ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/freqvisitor
curl -XPUT "http://$ELASTIC/$INDEX/_mapping/freqvisitor" -d '
{
     "freqvisitor" : {
            "properties" : {
            	"namespaceId" : {
                    "type": "long"
                },
                "ownerType" : {
                    "type": "string",
                    "index": "not_analyzed"
                },
                "ownerId" : {
                    "type": "long"
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
                    "type": "string"
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
                 "idNumber" : {
                     "type": "string",
                     "index": "not_analyzed"
                  }

            }
          }

}
'