//
// EvhNewsListNewsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListNewsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsListNewsRestResponse
//
@interface EvhNewsListNewsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListNewsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
