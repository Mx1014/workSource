//
// EvhQueryOrganizationTopicCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryOrganizationTopicCommand
//
@interface EvhQueryOrganizationTopicCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSNumber* actionCategory;

@property(nonatomic, copy) NSString* publishStatus;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* embeddedAppId;

@property(nonatomic, copy) NSNumber* officialFlag;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* excludeCategories;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

