//
// EvhTechparkPunchListPunchStatisticsRestResponse.h
// generated at 2016-03-25 09:26:45 
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
