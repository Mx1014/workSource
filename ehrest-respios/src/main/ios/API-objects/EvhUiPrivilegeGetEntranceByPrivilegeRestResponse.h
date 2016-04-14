//
// EvhUiPrivilegeGetEntranceByPrivilegeRestResponse.h
// generated at 2016-04-12 19:00:53 
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
