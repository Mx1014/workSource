//
// EvhVersionGetVersionUrlsRestResponse.h
// generated at 2016-03-25 11:43:35 
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
