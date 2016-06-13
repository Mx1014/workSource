//
// EvhPmsyCommunityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyCommunityDTO
//
@interface EvhPmsyCommunityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* billTip;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

