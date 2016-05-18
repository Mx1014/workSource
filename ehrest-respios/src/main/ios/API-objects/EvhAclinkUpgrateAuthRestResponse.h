//
// EvhAclinkUpgrateAuthRestResponse.h
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
