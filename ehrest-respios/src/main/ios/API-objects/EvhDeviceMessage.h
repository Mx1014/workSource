//
// EvhDeviceMessage.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeviceMessage
//
@interface EvhDeviceMessage
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* alert;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* icon;

@property(nonatomic, copy) NSString* audio;

@property(nonatomic, copy) NSString* alertType;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* timeLive;

@property(nonatomic, copy) NSString* action;

@property(nonatomic, copy) NSNumber* appId;

@property(nonatomic, copy) NSNumber* badge;

// item type NSString*
@property(nonatomic, strong) NSMutableDictionary* extra;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

