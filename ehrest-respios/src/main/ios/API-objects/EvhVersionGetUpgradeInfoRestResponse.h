//
// EvhVersionGetUpgradeInfoRestResponse.h
// generated at 2016-04-12 15:02:21 
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
