//
// EvhListUserFavoriteTopicCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserFavoriteTopicCommand
//
@interface EvhListUserFavoriteTopicCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* excludeCategories;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

