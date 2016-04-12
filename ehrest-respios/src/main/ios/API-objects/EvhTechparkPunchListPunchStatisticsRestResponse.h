//
// EvhTechparkPunchListPunchStatisticsRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhListPunchStatisticsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkPunchListPunchStatisticsRestResponse
//
@interface EvhTechparkPunchListPunchStatisticsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPunchStatisticsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
