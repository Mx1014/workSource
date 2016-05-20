//
// EvhOrganizationMemberDetailDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationMemberDetailDTO
//
@interface EvhOrganizationMemberDetailDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* memberGroup;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* contactDescription;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSNumber* employeeNo;

@property(nonatomic, copy) NSNumber* gender;

@property(nonatomic, strong) EvhOrganizationDetailDTO* organizationDetailDTO;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

