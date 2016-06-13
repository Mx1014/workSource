//
// EvhQualityCreatQualityStandardRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhQualityStandardsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityCreatQualityStandardRestResponse
//
@interface EvhQualityCreatQualityStandardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQualityStandardsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
