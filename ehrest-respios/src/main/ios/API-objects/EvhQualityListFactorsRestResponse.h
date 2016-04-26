//
// EvhQualityListFactorsRestResponse.h
// generated at 2016-04-26 18:22:57 
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
