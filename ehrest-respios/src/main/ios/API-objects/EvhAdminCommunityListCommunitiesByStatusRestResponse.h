//
// EvhAdminCommunityListCommunitiesByStatusRestResponse.h
// generated at 2016-03-25 11:43:34 
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
