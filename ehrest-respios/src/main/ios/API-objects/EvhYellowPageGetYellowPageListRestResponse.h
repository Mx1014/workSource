//
// EvhYellowPageGetYellowPageListRestResponse.h
// generated at 2016-03-25 19:05:21 
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
