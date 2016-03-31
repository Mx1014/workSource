//
// EvhBusinessMessageCommand.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessMessageCommand
//
@interface EvhBusinessMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* nonce;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* content;

// item type NSString*
@property(nonatomic, strong) NSMutableDictionary* meta;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

