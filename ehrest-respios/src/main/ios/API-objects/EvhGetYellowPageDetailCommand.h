//
// EvhGetYellowPageDetailCommand.h
// generated at 2016-03-31 13:49:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetYellowPageDetailCommand
//
@interface EvhGetYellowPageDetailCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* type;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

