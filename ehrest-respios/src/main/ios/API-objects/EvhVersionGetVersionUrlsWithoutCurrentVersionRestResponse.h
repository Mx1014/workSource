//
// EvhVersionGetVersionUrlsWithoutCurrentVersionRestResponse.h
// generated at 2016-04-05 13:45:27 
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
