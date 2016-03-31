//
// EvhConfListSourceVideoConfAccountRestResponse.h
// generated at 2016-03-31 13:49:15 
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
