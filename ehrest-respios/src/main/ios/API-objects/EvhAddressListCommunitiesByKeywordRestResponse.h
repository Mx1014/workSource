//
// EvhAddressListCommunitiesByKeywordRestResponse.h
// generated at 2016-04-05 13:45:26 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressListCommunitiesByKeywordRestResponse
//
@interface EvhAddressListCommunitiesByKeywordRestResponse : EvhRestResponseBase

// array of EvhCommunityDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
