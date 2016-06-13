//
// EvhVersionGetVersionUrlsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhVersionUrlResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionGetVersionUrlsRestResponse
//
@interface EvhVersionGetVersionUrlsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVersionUrlResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
