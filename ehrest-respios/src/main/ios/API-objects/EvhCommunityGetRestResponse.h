//
// EvhCommunityGetRestResponse.h
// generated at 2016-04-18 14:48:52 
//
#import "RestResponseBase.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityGetRestResponse
//
@interface EvhCommunityGetRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCommunityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
