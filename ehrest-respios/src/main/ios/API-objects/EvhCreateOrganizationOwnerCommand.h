//
// EvhCreateOrganizationOwnerCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationOwnerCommand
//
@interface EvhCreateOrganizationOwnerCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* contactDescription;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* apartmentName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

