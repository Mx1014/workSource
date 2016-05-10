//
// EvhAdminCommunityGetCommunityManagersRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminCommunityGetCommunityManagersRestResponse
//
@interface EvhAdminCommunityGetCommunityManagersRestResponse : EvhRestResponseBase

// array of EvhCommunityManagerDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
