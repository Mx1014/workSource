//
// EvhNewsSearchNewsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNewsListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsSearchNewsRestResponse
//
@interface EvhNewsSearchNewsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNewsListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
