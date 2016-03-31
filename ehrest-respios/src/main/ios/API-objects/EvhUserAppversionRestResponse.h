//
// EvhUserAppversionRestResponse.h
// generated at 2016-03-31 13:49:15 
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
