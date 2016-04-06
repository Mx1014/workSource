//
// EvhPmGetUserOwningPropertiesRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListPropMemberCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmGetUserOwningPropertiesRestResponse
//
@interface EvhPmGetUserOwningPropertiesRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListPropMemberCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
