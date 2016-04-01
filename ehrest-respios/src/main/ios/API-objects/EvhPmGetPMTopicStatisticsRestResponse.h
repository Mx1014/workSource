//
// EvhPmGetPMTopicStatisticsRestResponse.h
// generated at 2016-03-31 20:15:33 
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
