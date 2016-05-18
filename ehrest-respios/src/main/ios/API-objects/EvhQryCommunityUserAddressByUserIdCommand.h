//
// EvhQryCommunityUserAddressByUserIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQryCommunityUserAddressByUserIdCommand
//
@interface EvhQryCommunityUserAddressByUserIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* userId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

