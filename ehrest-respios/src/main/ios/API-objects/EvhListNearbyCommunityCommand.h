//
// EvhListNearbyCommunityCommand.h
// generated at 2016-04-19 12:41:52 
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

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

