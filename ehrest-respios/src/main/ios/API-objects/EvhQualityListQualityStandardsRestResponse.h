//
// EvhQualityListQualityStandardsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListQualityStandardsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListQualityStandardsRestResponse
//
@interface EvhQualityListQualityStandardsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListQualityStandardsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
