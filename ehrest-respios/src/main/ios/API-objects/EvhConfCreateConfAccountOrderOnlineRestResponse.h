//
// EvhConfCreateConfAccountOrderOnlineRestResponse.h
// generated at 2016-04-12 15:02:21 
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
