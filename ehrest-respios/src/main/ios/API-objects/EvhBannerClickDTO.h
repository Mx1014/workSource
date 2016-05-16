//
// EvhBannerClickDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerClickDTO
//
@interface EvhBannerClickDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* bannerId;

@property(nonatomic, copy) NSNumber* uid;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSNumber* clickCount;

@property(nonatomic, copy) NSNumber* lastClickTime;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

