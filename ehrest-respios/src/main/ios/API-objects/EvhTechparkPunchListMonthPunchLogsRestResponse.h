//
// EvhTechparkPunchListMonthPunchLogsRestResponse.h
// generated at 2016-03-25 15:57:24 
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
