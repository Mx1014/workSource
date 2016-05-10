//
// EvhQualityListFactorsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListFactorsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListFactorsRestResponse
//
@interface EvhQualityListFactorsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListFactorsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
