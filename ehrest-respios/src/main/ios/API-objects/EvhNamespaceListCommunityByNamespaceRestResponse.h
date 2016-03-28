//
// EvhNamespaceListCommunityByNamespaceRestResponse.h
// generated at 2016-03-25 19:05:21 
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
