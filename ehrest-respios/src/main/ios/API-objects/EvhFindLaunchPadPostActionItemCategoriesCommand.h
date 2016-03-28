//
// EvhFindLaunchPadPostActionItemCategoriesCommand.h
// generated at 2016-03-28 15:56:09 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindLaunchPadPostActionItemCategoriesCommand
//
@interface EvhFindLaunchPadPostActionItemCategoriesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSString* itemGroup;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

