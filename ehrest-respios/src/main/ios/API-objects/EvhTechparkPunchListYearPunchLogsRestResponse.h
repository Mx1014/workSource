//
// EvhTechparkPunchListYearPunchLogsRestResponse.h
// generated at 2016-03-25 17:08:13 
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
