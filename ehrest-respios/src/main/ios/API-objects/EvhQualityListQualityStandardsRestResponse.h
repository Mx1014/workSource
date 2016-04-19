//
// EvhQualityListQualityStandardsRestResponse.h
// generated at 2016-04-19 12:41:55 
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
