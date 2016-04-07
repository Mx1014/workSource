//
// EvhTechparkPunchGetDayPunchLogsRestResponse.h
// generated at 2016-04-07 15:16:54 
//
#import "RestResponseBase.h"
#import "EvhPunchLogsDay.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchGetDayPunchLogsRestResponse
//
@interface EvhTechparkPunchGetDayPunchLogsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPunchLogsDay* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
