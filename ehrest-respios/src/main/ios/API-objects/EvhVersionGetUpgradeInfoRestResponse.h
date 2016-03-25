//
// EvhVersionGetUpgradeInfoRestResponse.h
// generated at 2016-03-25 11:43:35 
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
