//
// EvhConfListWarningContactorRestResponse.h
// generated at 2016-04-19 13:40:01 
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
