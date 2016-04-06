//
// EvhOrgApplyForEnterpriseContactRestResponse.h
// generated at 2016-04-06 19:10:43 
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
