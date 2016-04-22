//
// EvhAclinkUpgrateAuthRestResponse.h
// generated at 2016-04-22 13:56:49 
//
#import "RestResponseBase.h"
#import "EvhAclinkUpgradeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUpgrateAuthRestResponse
//
@interface EvhAclinkUpgrateAuthRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhAclinkUpgradeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
