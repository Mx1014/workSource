//
// EvhConfUpdateConfAccountPeriodRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"
#import "EvhConfAccountOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfUpdateConfAccountPeriodRestResponse
//
@interface EvhConfUpdateConfAccountPeriodRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhConfAccountOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
