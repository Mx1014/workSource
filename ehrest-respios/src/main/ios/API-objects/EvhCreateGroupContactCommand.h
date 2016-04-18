//
// EvhCreateGroupContactCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateGroupContactCommand
//
@interface EvhCreateGroupContactCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* department;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

