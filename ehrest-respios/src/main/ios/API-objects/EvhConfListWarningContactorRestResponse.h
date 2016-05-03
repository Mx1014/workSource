//
// EvhConfListWarningContactorRestResponse.h
// generated at 2016-04-29 18:56:03 
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
