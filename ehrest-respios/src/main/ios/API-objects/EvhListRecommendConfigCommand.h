//
// EvhListRecommendConfigCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRecommendConfigCommand
//
@interface EvhListRecommendConfigCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSNumber* suggestType;

@property(nonatomic, copy) NSNumber* sourceType;

@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSNumber* sourceId;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

