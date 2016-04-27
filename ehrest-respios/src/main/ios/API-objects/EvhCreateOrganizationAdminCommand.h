//
// EvhCreateOrganizationAdminCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationAdminCommand
//
@interface EvhCreateOrganizationAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* contactName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

