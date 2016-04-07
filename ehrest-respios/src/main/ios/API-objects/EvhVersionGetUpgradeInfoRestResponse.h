//
// EvhVersionGetUpgradeInfoRestResponse.h
// generated at 2016-04-07 10:47:33 
//
#import "RestResponseBase.h"
#import "EvhUpgradeInfoResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionGetUpgradeInfoRestResponse
//
@interface EvhVersionGetUpgradeInfoRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhUpgradeInfoResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
