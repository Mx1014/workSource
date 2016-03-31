//
// EvhAddressListSuggestedCommunitiesRestResponse.h
// generated at 2016-03-31 13:49:15 
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
