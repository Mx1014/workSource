//
// EvhNotifyDoorLockCommand.h
// generated at 2016-03-25 17:08:12 
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

