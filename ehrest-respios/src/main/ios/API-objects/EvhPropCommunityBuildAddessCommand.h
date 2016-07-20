//
// EvhPropCommunityBuildAddessCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropCommunityBuildAddessCommand
//
@interface EvhPropCommunityBuildAddessCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* buildingNames;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* buildingIds;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* addressIds;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* mobilePhones;

@property(nonatomic, copy) NSString* message;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

