//
// EvhYellowPageGetYellowPageTopicRestResponse.h
// generated at 2016-03-25 09:26:45 
//
#import "RestResponseBase.h"
#import "EvhYellowPageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhYellowPageGetYellowPageTopicRestResponse
//
@interface EvhYellowPageGetYellowPageTopicRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhYellowPageDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
