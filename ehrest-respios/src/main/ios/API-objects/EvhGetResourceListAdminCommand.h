//
// EvhGetResourceListAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetResourceListAdminCommand
//
@interface EvhGetResourceListAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* resourceTypeId;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

