//
// EvhAddressListCommunitiesByKeywordRestResponse.h
// generated at 2016-04-07 10:47:32 
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
