//
// EvhYellowPageGetYellowPageDetailRestResponse.h
// generated at 2016-03-28 15:56:09 
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
