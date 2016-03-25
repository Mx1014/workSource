//
// EvhAddressSuggestCommunityRestResponse.h
// generated at 2016-03-25 11:43:34 
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
