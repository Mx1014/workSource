//
// EvhNamespaceListCommunityByNamespaceRestResponse.h
// generated at 2016-04-18 14:48:52 
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
