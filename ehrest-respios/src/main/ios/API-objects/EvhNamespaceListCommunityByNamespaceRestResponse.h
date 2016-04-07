//
// EvhNamespaceListCommunityByNamespaceRestResponse.h
// generated at 2016-04-07 10:47:33 
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
