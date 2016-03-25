//
// EvhPmGetUserOwningPropertiesRestResponse.h
// generated at 2016-03-25 09:26:44 
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
