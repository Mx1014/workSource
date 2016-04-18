//
// EvhYellowPageGetYellowPageDetailRestResponse.h
// generated at 2016-04-18 14:48:53 
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
