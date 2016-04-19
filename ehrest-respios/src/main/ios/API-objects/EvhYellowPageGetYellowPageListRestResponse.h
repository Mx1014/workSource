//
// EvhYellowPageGetYellowPageListRestResponse.h
// generated at 2016-04-19 13:40:02 
//
#import "RestResponseBase.h"
#import "EvhYellowPageListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhYellowPageGetYellowPageListRestResponse
//
@interface EvhYellowPageGetYellowPageListRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhYellowPageListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
