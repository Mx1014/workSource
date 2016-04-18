//
// EvhQualityUpdateQualityStandardRestResponse.h
// generated at 2016-04-18 14:48:52 
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
