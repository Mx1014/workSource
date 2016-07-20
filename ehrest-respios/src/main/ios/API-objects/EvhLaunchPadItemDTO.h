//
// EvhLaunchPadItemDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadItemDTO
//
@interface EvhLaunchPadItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSString* scopeType;

@property(nonatomic, copy) NSNumber* scopeId;

@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSString* itemGroup;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSString* itemLabel;

@property(nonatomic, copy) NSString* iconUri;

@property(nonatomic, copy) NSString* iconUrl;

@property(nonatomic, copy) NSNumber* itemWidth;

@property(nonatomic, copy) NSNumber* itemHeight;

@property(nonatomic, copy) NSNumber* actionType;

@property(nonatomic, copy) NSString* actionData;

@property(nonatomic, copy) NSNumber* defaultOrder;

@property(nonatomic, copy) NSNumber* applyPolicy;

@property(nonatomic, copy) NSNumber* minVersion;

@property(nonatomic, copy) NSNumber* displayFlag;

@property(nonatomic, copy) NSString* displayLayout;

@property(nonatomic, copy) NSNumber* bgcolor;

@property(nonatomic, copy) NSNumber* scaleType;

@property(nonatomic, copy) NSNumber* deleteFlag;

@property(nonatomic, copy) NSNumber* editFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

