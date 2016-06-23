//
// EvhActivityGetActivityShareDetailRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhActivityShareDetailResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityGetActivityShareDetailRestResponse
//
@interface EvhActivityGetActivityShareDetailRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhActivityShareDetailResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
