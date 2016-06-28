//
// EvhHotTagSetHotTagRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhTagDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhHotTagSetHotTagRestResponse
//
@interface EvhHotTagSetHotTagRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhTagDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
