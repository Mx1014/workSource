//
// EvhUpdateEnterpriseCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationAddressDTO.h"
#import "EvhAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateEnterpriseCommand
//
@interface EvhUpdateEnterpriseCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* memberCount;

@property(nonatomic, copy) NSString* contactsPhone;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSString* entries;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* checkinDate;

@property(nonatomic, copy) NSString* postUri;

@property(nonatomic, copy) NSNumber* namespaceId;

// item type EvhOrganizationAddressDTO*
@property(nonatomic, strong) NSMutableArray* addressDTOs;

// item type EvhAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

