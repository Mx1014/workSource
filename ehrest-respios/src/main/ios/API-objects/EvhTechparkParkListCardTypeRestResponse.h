//
// EvhTechparkParkListCardTypeRestResponse.h
// generated at 2016-04-07 17:57:44 
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
