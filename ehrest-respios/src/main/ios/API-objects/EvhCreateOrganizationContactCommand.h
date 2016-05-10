//
// EvhCreateOrganizationContactCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationContactCommand
//
@interface EvhCreateOrganizationContactCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

