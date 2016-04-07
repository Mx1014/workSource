//
// EvhTechparkPunchListMonthPunchLogsRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"
#import "EvhListMonthPunchLogsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchListMonthPunchLogsRestResponse
//
@interface EvhTechparkPunchListMonthPunchLogsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListMonthPunchLogsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
