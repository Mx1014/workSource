
ELASTIC=elasticsearch:9200
INDEX=everhomesv3

curl -XDELETE http://$ELASTIC/$INDEX/_mapping/saRequest

curl -XPUT "http://$ELASTIC/$INDEX/_mapping/saRequest" -d '
{
     "saRequest" : {
            "properties" : {
            	"createDate" : {
				  "type" : "long"
				},
				"createTime" : {
				  "type" : "long"
				},
				"creatorMobile" : {
				  "type" : "string"
				},
				"creatorName" : {
				  "type" : "string"
				},
				"creatorOrganization" : {
				  "type" : "string"
				},
				"creatorOrganizationId" : {
				  "type" : "long"
				},
				"creatorUid" : {
				  "type" : "long"
				},
				"flowCaseId" : {
				  "type" : "long"
				},
				"jumpType" : {
				  "type" : "long"
				},
				"ownerId" : {
				  "type" : "long"
				},
				"ownerType" : {
				  "type" : "string"
				},
				"serviceOrganization" : {
				  "type" : "string"
				},
				"templateType" : {
				  "type" : "string"
				},
				"type" : {
				  "type" : "long"
				},
				"secondCategoryId" : {
				  "type" : "long"
				},
				"secondCategoryName" : {
				  "type" : "string"
				},
				"workflowStatus" : {
				  "type" : "long"
				}
            }
          }

}
'
