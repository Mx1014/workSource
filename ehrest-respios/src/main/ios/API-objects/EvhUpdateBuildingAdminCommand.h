//
// EvhUpdateBuildingAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateBuildingAdminCommand
//
@interface EvhUpdateBuildingAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* aliasName;

@property(nonatomic, copy) NSNumber* managerUid;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* areaSize;

@property(nonatomic, copy) NSString* geoString;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* posterUri;

@property(nonatomic, copy) NSNumber* namespaceId;

// item type EvhAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

