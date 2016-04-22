//
// EvhUserProfileDTO.h
// generated at 2016-04-22 13:56:45 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserProfileDTO
//
@interface EvhUserProfileDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSNumber* itemKind;

@property(nonatomic, copy) NSString* itemValue;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

