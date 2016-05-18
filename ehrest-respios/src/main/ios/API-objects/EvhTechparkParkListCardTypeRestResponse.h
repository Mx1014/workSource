//
// EvhTechparkParkListCardTypeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCardTypeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkListCardTypeRestResponse
//
@interface EvhTechparkParkListCardTypeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCardTypeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
