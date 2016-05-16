//
// EvhBusinessCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessCommand
//
@interface EvhBusinessCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* targetType;

@property(nonatomic, copy) NSString* targetId;

@property(nonatomic, copy) NSNumber* bizOwnerUid;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSString* logoUri;

@property(nonatomic, copy) NSString* url;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* description_;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* categroies;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

