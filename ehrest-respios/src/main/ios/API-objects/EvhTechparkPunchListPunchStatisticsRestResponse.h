//
// EvhTechparkPunchListPunchStatisticsRestResponse.h
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
