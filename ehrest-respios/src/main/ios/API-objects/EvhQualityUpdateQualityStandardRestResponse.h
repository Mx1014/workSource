//
// EvhQualityUpdateQualityStandardRestResponse.h
// generated at 2016-04-22 13:56:51 
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
