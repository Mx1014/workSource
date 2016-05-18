//
// EvhTechparkPunchListMonthPunchLogsRestResponse.h
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
