//
// EvhConfListSourceVideoConfAccountRestResponse.h
// generated at 2016-04-06 19:10:43 
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
