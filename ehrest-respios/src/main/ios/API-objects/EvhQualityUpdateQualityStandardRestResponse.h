//
// EvhQualityUpdateQualityStandardRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhQualityStandardsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityUpdateQualityStandardRestResponse
//
@interface EvhQualityUpdateQualityStandardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQualityStandardsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
