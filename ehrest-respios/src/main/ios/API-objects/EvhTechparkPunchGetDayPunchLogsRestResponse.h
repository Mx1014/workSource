//
// EvhTechparkPunchGetDayPunchLogsRestResponse.h
// generated at 2016-03-25 11:43:35 
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
