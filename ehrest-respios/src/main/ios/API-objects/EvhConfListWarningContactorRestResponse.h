//
// EvhConfListWarningContactorRestResponse.h
// generated at 2016-03-31 15:43:24 
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
