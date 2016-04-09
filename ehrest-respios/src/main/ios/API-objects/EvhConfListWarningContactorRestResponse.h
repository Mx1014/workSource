//
// EvhConfListWarningContactorRestResponse.h
// generated at 2016-04-08 20:09:23 
//
#import "RestResponseBase.h"
#import "EvhListWarningContactorResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListWarningContactorRestResponse
//
@interface EvhConfListWarningContactorRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListWarningContactorResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
