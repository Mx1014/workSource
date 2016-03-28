//
// EvhEnterpriseSearchCommunitiesRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseSearchCommunitiesRestResponse
//
@interface EvhEnterpriseSearchCommunitiesRestResponse : EvhRestResponseBase

// array of EvhCommunityDoc* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
