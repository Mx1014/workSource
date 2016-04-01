//
// EvhPmGetPmPayStatisticsRestResponse.h
// generated at 2016-03-31 20:15:33 
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
