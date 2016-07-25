//
// EvhUpdateResourceAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSiteOwnerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateResourceAdminCommand
//
@interface EvhUpdateResourceAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* spec;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* contactPhonenum;

@property(nonatomic, copy) NSNumber* chargeUid;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

@property(nonatomic, copy) NSString* coverUri;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* detailUris;

// item type EvhSiteOwnerDTO*
@property(nonatomic, strong) NSMutableArray* owners;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

