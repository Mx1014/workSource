//
// EvhUpdateLaunchPadItemAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateLaunchPadItemAdminCommand
//
@interface EvhUpdateLaunchPadItemAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSNumber* scopeId;

@property(nonatomic, copy) NSNumber* defaultOrder;

@property(nonatomic, copy) NSNumber* applyPolicy;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSString* itemLabel;

@property(nonatomic, copy) NSString* itemGroup;

@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSNumber* itemWidth;

@property(nonatomic, copy) NSNumber* itemHeight;

@property(nonatomic, copy) NSString* iconUri;

@property(nonatomic, copy) NSNumber* actionType;

@property(nonatomic, copy) NSString* actionData;

@property(nonatomic, copy) NSNumber* displayFlag;

@property(nonatomic, copy) NSString* displayLayout;

@property(nonatomic, copy) NSString* tag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

