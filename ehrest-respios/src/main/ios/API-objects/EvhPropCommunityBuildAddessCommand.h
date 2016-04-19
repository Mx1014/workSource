//
// EvhPropCommunityBuildAddessCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
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

