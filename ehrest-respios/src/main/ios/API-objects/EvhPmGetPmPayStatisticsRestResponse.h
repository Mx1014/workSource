//
// EvhPmGetPmPayStatisticsRestResponse.h
// generated at 2016-03-28 15:56:09 
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
