//
// EvhTechparkPunchListYearPunchLogsRestResponse.h
// generated at 2016-04-08 20:09:24 
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
