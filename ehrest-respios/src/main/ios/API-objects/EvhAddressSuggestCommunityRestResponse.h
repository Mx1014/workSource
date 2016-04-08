//
// EvhAddressSuggestCommunityRestResponse.h
// generated at 2016-04-08 20:09:23 
//
#import "RestResponseBase.h"
#import "EvhSuggestCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressSuggestCommunityRestResponse
//
@interface EvhAddressSuggestCommunityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSuggestCommunityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
