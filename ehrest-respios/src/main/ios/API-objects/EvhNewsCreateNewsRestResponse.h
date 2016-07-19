//
// EvhNewsCreateNewsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCreateNewsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsCreateNewsRestResponse
//
@interface EvhNewsCreateNewsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCreateNewsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
