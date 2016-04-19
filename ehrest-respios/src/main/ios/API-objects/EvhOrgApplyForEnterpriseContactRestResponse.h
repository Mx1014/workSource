//
// EvhOrgApplyForEnterpriseContactRestResponse.h
// generated at 2016-04-19 13:40:01 
//
#import "RestResponseBase.h"
#import "EvhOrganizationMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrgApplyForEnterpriseContactRestResponse
//
@interface EvhOrgApplyForEnterpriseContactRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhOrganizationMemberDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
