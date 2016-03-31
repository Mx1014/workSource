//
// EvhAddressSuggestCommunityRestResponse.h
// generated at 2016-03-28 15:56:09 
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
