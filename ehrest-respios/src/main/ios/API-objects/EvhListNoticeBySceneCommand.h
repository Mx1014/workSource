//
// EvhListNoticeBySceneCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNoticeBySceneCommand
//
@interface EvhListNoticeBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSString* publishStatus;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* excludeCategories;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

