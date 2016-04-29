//
// EvhUpdateRentalSiteCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRentalSiteCommand
//
@interface EvhUpdateRentalSiteCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalSiteId;

@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* spec;

@property(nonatomic, copy) NSString* company;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactPhonenum;

@property(nonatomic, copy) NSString* introduction;

@property(nonatomic, copy) NSString* notice;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

