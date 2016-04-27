//
// EvhNotifyDoorLockCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyDoorLockCommand
//
@interface EvhNotifyDoorLockCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* nonce;

@property(nonatomic, copy) NSString* crypto;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* Phones;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

