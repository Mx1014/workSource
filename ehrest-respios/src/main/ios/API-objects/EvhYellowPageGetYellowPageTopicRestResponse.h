//
// EvhYellowPageGetYellowPageTopicRestResponse.h
// generated at 2016-03-31 20:15:34 
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
