//
// EvhVersionGetVersionUrlsRestResponse.h
// generated at 2016-04-06 19:10:44 
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
