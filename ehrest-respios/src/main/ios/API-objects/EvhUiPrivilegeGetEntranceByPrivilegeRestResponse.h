//
// EvhUiPrivilegeGetEntranceByPrivilegeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetEntranceByPrivilegeResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiPrivilegeGetEntranceByPrivilegeRestResponse
//
@interface EvhUiPrivilegeGetEntranceByPrivilegeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetEntranceByPrivilegeResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
