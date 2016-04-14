//
// EvhQualityListEvaluationsRestResponse.h
// generated at 2016-04-12 19:00:53 
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
