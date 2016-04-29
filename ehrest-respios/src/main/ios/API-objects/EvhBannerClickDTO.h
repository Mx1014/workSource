//
// EvhBannerClickDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

