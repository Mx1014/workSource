//
// EvhAddressListCommunitiesByKeywordRestResponse.h
// generated at 2016-04-26 18:22:56 
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
