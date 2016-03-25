//
// EvhTechparkPunchGetDayPunchLogsRestResponse.h
// generated at 2016-03-25 17:08:13 
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
