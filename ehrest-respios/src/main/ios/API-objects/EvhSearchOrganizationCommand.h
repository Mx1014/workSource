//
// EvhSearchOrganizationCommand.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchOrganizationCommand
//
@interface EvhSearchOrganizationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

