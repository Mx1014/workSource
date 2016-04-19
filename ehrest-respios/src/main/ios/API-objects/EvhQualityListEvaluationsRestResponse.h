//
// EvhQualityListEvaluationsRestResponse.h
// generated at 2016-04-19 14:25:58 
//
#import "RestResponseBase.h"
#import "EvhListEvaluationsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListEvaluationsRestResponse
//
@interface EvhQualityListEvaluationsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListEvaluationsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
