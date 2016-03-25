//
// EvhPmGetPMTopicStatisticsRestResponse.h
// generated at 2016-03-25 09:26:44 
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
