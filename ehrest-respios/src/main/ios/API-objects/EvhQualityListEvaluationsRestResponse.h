//
// EvhQualityListEvaluationsRestResponse.h
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
