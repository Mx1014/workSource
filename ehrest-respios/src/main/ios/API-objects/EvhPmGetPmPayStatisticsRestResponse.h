//
// EvhPmGetPmPayStatisticsRestResponse.h
// generated at 2016-03-25 11:43:35 
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
