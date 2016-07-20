//
// EvhContactDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContactDTO
//
@interface EvhContactDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* contactPhone;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSNumber* contactId;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* ehUsername;

@property(nonatomic, copy) NSString* contactAvatar;

@property(nonatomic, copy) NSString* communityName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

