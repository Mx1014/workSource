//
// EvhAdminCommunityListCommunitiesByStatusRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhListCommunitesByStatusCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityListCommunitiesByStatusRestResponse
//
@interface EvhAdminCommunityListCommunitiesByStatusRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCommunitesByStatusCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
