//
// EvhNotifyDoorMessageCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyDoorMessageCommand
//
@interface EvhNotifyDoorMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* nonce;

@property(nonatomic, copy) NSString* crypto;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* Phones;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

