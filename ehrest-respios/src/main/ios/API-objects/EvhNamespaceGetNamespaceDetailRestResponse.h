//
// EvhNamespaceGetNamespaceDetailRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNamespaceDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNamespaceGetNamespaceDetailRestResponse
//
@interface EvhNamespaceGetNamespaceDetailRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNamespaceDetailDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
