//
// EvhConfListSourceVideoConfAccountRestResponse.h
// generated at 2016-04-29 18:56:03 
//
#import "RestResponseBase.h"
#import "EvhListSourceVideoConfAccountResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhConfListSourceVideoConfAccountRestResponse
//
@interface EvhConfListSourceVideoConfAccountRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListSourceVideoConfAccountResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
