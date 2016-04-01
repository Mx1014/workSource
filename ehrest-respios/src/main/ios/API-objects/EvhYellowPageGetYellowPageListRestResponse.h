//
// EvhYellowPageGetYellowPageListRestResponse.h
// generated at 2016-04-01 15:40:24 
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
