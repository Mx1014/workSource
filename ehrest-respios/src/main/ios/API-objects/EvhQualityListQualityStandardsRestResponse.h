//
// EvhQualityListQualityStandardsRestResponse.h
// generated at 2016-04-29 18:56:04 
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
