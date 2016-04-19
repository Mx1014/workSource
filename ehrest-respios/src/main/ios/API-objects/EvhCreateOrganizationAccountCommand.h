//
// EvhCreateOrganizationAccountCommand.h
// generated at 2016-04-19 13:39:59 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationAccountCommand
//
@interface EvhCreateOrganizationAccountCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* accountPhone;

@property(nonatomic, copy) NSString* accountName;

@property(nonatomic, copy) NSNumber* assignmentId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

