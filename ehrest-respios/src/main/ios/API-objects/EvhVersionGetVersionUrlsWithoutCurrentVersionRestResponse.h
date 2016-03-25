//
// EvhVersionGetVersionUrlsWithoutCurrentVersionRestResponse.h
// generated at 2016-03-25 09:26:45 
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
