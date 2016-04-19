//
// EvhAclinkUpgrateAuthRestResponse.h
// generated at 2016-04-19 12:41:54 
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
