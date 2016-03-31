//
// EvhAddressListCommunitiesByKeywordRestResponse.h
// generated at 2016-03-31 11:07:27 
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
