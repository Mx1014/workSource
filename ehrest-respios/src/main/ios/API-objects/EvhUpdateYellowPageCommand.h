//
// EvhUpdateYellowPageCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhYellowPageAattchmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateYellowPageCommand
//
@interface EvhUpdateYellowPageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSNumber* type;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* posterUri;

@property(nonatomic, copy) NSString* posterUrl;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* defaultOrder;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* geohash;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactMobile;

@property(nonatomic, copy) NSString* serviceType;

// item type EvhYellowPageAattchmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

