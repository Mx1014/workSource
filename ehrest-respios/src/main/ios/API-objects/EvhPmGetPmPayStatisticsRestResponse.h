//
// EvhPmGetPmPayStatisticsRestResponse.h
// generated at 2016-04-07 17:33:50 
//
#import "RestResponseBase.h"
#import "EvhGetPmPayStatisticsCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmGetPmPayStatisticsRestResponse
//
@interface EvhPmGetPmPayStatisticsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetPmPayStatisticsCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
