//
// EvhCreateLaunchPadItemCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhItemScope.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLaunchPadItemCommand
//
@interface EvhCreateLaunchPadItemCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* appId;

// item type EvhItemScope*
@property(nonatomic, strong) NSMutableArray* itemScopes;

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

@property(nonatomic, copy) NSNumber* bgcolor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

