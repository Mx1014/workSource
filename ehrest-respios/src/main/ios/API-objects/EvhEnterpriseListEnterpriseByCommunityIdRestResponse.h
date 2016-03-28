//
// EvhEnterpriseListEnterpriseByCommunityIdRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhListEnterpriseResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseListEnterpriseByCommunityIdRestResponse
//
@interface EvhEnterpriseListEnterpriseByCommunityIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEnterpriseResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
