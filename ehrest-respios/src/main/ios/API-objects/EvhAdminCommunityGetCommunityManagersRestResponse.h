//
// EvhAdminCommunityGetCommunityManagersRestResponse.h
// generated at 2016-03-25 17:08:12 
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
