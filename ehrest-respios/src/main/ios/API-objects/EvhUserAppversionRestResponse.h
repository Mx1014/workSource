//
// EvhUserAppversionRestResponse.h
// generated at 2016-03-31 11:07:27 
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
