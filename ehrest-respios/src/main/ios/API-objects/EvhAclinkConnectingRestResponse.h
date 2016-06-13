//
// EvhAclinkConnectingRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhDoorAccessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkConnectingRestResponse
//
@interface EvhAclinkConnectingRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhDoorAccessDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
