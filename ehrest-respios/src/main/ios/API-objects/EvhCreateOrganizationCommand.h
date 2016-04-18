//
// EvhCreateOrganizationCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationCommand
//
@interface EvhCreateOrganizationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* groupType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

