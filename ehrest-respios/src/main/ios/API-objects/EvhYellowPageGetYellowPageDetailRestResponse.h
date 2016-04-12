//
// EvhYellowPageGetYellowPageDetailRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhYellowPageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhYellowPageGetYellowPageDetailRestResponse
//
@interface EvhYellowPageGetYellowPageDetailRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhYellowPageDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
