//
// EvhListAclinkUserCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListAclinkUserCommand
//
@interface EvhListAclinkUserCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSNumber* isAuth;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

