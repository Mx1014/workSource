//
// EvhQualityListQualityCategoriesRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhListQualityCategoriesResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListQualityCategoriesRestResponse
//
@interface EvhQualityListQualityCategoriesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListQualityCategoriesResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
