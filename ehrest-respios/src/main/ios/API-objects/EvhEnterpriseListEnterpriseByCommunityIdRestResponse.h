//
// EvhEnterpriseListEnterpriseByCommunityIdRestResponse.h
// generated at 2016-04-19 14:25:58 
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
