//
// EvhQualityListQualityStandardsRestResponse.h
// generated at 2016-04-26 18:22:57 
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
