//
// EvhLaunchPadPostActionCategoryDTO.h
// generated at 2016-03-25 19:05:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadPostActionCategoryDTO
//
@interface EvhLaunchPadPostActionCategoryDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSString* itemLabel;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSNumber* actionCategory;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

