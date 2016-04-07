//
// EvhVersionGetVersionUrlsWithoutCurrentVersionRestResponse.h
// generated at 2016-04-07 10:47:33 
//
#import "RestResponseBase.h"
#import "EvhVersionUrlResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionGetVersionUrlsWithoutCurrentVersionRestResponse
//
@interface EvhVersionGetVersionUrlsWithoutCurrentVersionRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhVersionUrlResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
