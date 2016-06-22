//
// EvhListNearbyCommunityCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyCommunityCommand
//
@interface EvhListNearbyCommunityCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latigtue;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* communityType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

