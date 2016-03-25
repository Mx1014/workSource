//
// EvhPostNewActionData.h
// generated at 2016-03-25 15:57:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPostNewActionData
//
@interface EvhPostNewActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* actionCategory;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSString* entityTag;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSString* creatorEntityTag;

@property(nonatomic, copy) NSString* targetEntityTag;

@property(nonatomic, copy) NSNumber* visibleRegionType;

@property(nonatomic, copy) NSNumber* visibleRegionId;

@property(nonatomic, copy) NSNumber* embedAppId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

