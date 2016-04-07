//
// EvhTechparkPunchListPunchStatisticsRestResponse.h
// generated at 2016-04-07 10:47:33 
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
