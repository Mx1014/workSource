//
// EvhQualityUpdateQualityStandardRestResponse.h
// generated at 2016-04-12 15:02:21 
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
