//
// EvhAddressListSuggestedCommunitiesRestResponse.h
// generated at 2016-04-07 14:16:31 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListSuggestedCommunitiesRestResponse
//
@interface EvhAddressListSuggestedCommunitiesRestResponse : EvhRestResponseBase

// array of EvhCommunitySummaryDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
