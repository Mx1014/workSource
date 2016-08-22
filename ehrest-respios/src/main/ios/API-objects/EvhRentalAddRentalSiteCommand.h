//
// EvhRentalAddRentalSiteCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAddRentalSiteCommand
//
@interface EvhRentalAddRentalSiteCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* spec;

@property(nonatomic, copy) NSString* company;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactPhonenum;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

@property(nonatomic, copy) NSString* siteItems;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

