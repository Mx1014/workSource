//
// EvhAddressSearchCommunitiesRestResponse.h
// generated at 2016-04-12 15:02:20 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressSearchCommunitiesRestResponse
//
@interface EvhAddressSearchCommunitiesRestResponse : EvhRestResponseBase

// array of EvhCommunityDoc* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
