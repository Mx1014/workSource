//
// EvhVersionGetUpgradeInfoRestResponse.h
// generated at 2016-03-28 15:56:09 
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
