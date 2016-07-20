//
// EvhBuildingForRentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBuildingForRentAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingForRentDTO
//
@interface EvhBuildingForRentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* rentPosition;

@property(nonatomic, copy) NSString* rentType;

@property(nonatomic, copy) NSString* posterUri;

@property(nonatomic, copy) NSString* posterUrl;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* rentAreas;

@property(nonatomic, copy) NSString* contacts;

@property(nonatomic, copy) NSString* contactPhone;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* enterTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* createTime;

// item type EvhBuildingForRentAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

