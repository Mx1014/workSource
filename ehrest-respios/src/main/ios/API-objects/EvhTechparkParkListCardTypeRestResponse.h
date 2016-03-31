//
// EvhTechparkParkListCardTypeRestResponse.h
// generated at 2016-03-31 19:08:54 
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
