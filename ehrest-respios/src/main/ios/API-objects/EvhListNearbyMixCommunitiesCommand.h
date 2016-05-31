//
// EvhListNearbyMixCommunitiesCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyMixCommunitiesCommand
//
@interface EvhListNearbyMixCommunitiesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latigtue;

@property(nonatomic, copy) NSNumber* communityType;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

