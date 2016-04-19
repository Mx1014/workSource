//
// EvhUpdateOrganizationContactCommand.h
// generated at 2016-04-19 12:41:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateOrganizationContactCommand
//
@interface EvhUpdateOrganizationContactCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

