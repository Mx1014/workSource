//
// EvhUiPrivilegeGetEntranceByPrivilegeRestResponse.h
// generated at 2016-03-25 15:57:24 
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
