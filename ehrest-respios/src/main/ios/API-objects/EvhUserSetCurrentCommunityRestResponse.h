//
// EvhUserSetCurrentCommunityRestResponse.h
// generated at 2016-04-29 18:56:04 
//
#import "RestResponseBase.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserSetCurrentCommunityRestResponse
//
@interface EvhUserSetCurrentCommunityRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
