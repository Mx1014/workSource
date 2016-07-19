//
// EvhNewsSearchNewsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSearchNewsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsSearchNewsRestResponse
//
@interface EvhNewsSearchNewsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchNewsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
