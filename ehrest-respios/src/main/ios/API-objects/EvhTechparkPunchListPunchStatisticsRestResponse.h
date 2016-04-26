//
// EvhTechparkPunchListPunchStatisticsRestResponse.h
// generated at 2016-04-26 18:22:57 
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
