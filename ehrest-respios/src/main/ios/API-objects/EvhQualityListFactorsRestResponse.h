//
// EvhQualityListFactorsRestResponse.h
// generated at 2016-04-12 15:02:21 
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
