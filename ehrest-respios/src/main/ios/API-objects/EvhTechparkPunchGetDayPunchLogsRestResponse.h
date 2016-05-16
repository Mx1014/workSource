//
// EvhTechparkPunchGetDayPunchLogsRestResponse.h
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
