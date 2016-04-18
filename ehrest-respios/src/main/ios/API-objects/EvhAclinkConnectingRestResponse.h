//
// EvhAclinkConnectingRestResponse.h
// generated at 2016-04-18 14:48:52 
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
