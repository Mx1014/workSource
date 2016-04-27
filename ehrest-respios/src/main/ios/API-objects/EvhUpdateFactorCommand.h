//
// EvhUpdateFactorCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateFactorCommand
//
@interface EvhUpdateFactorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSNumber* weight;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

