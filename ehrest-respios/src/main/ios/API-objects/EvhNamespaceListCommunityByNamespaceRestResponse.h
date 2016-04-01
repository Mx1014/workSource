//
// EvhNamespaceListCommunityByNamespaceRestResponse.h
// generated at 2016-04-01 15:40:24 
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
