//
// EvhUserSetCurrentCommunityRestResponse.h
// generated at 2016-03-31 20:15:34 
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
