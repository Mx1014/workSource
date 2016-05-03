//
// EvhYellowPageGetYellowPageListRestResponse.h
// generated at 2016-04-29 18:56:04 
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
