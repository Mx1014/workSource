//
// EvhUpdatePropAddressMappingCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePropAddressMappingCommand
//
@interface EvhUpdatePropAddressMappingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* name;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

