//
// EvhYellowPageGetYellowPageListRestResponse.h
// generated at 2016-03-25 11:43:35 
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
