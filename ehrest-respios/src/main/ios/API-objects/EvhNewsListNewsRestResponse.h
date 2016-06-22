//
// EvhNewsListNewsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNewsListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsListNewsRestResponse
//
@interface EvhNewsListNewsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNewsListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
