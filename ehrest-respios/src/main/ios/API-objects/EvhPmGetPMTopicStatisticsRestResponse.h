//
// EvhPmGetPMTopicStatisticsRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListPropTopicStatisticCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmGetPMTopicStatisticsRestResponse
//
@interface EvhPmGetPMTopicStatisticsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPropTopicStatisticCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
