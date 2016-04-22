//
// EvhConfUpdateConfAccountPeriodRestResponse.h
// generated at 2016-04-22 13:56:50 
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
