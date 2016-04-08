//
// EvhNamespaceListCommunityByNamespaceRestResponse.h
// generated at 2016-04-08 20:09:23 
//
#import "RestResponseBase.h"
#import "EvhListCommunityByNamespaceCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNamespaceListCommunityByNamespaceRestResponse
//
@interface EvhNamespaceListCommunityByNamespaceRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunityByNamespaceCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
