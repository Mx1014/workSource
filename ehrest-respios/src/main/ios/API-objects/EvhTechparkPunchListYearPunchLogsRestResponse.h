//
// EvhTechparkPunchListYearPunchLogsRestResponse.h
// generated at 2016-04-07 10:47:33 
//
#import "RestResponseBase.h"
#import "EvhListYearPunchLogsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchListYearPunchLogsRestResponse
//
@interface EvhTechparkPunchListYearPunchLogsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListYearPunchLogsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
