//
// EvhUserAppversionRestResponse.h
// generated at 2016-04-22 13:56:52 
//
#import "RestResponseBase.h"
#import "EvhGetAppVersion.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserAppversionRestResponse
//
@interface EvhUserAppversionRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetAppVersion* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
