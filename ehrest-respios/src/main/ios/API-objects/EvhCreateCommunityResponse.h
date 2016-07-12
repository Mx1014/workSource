//
// EvhCreateCommunityResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateCommunityResponse
//
@interface EvhCreateCommunityResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhCommunityDTO* communityDTO;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

