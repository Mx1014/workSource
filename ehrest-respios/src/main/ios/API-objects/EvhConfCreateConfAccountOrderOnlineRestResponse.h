//
// EvhConfCreateConfAccountOrderOnlineRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhConfAccountOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfCreateConfAccountOrderOnlineRestResponse
//
@interface EvhConfCreateConfAccountOrderOnlineRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhConfAccountOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
