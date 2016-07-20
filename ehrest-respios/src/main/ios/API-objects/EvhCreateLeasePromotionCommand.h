//
// EvhCreateLeasePromotionCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBuildingForRentAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLeasePromotionCommand
//
@interface EvhCreateLeasePromotionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* rentPosition;

@property(nonatomic, copy) NSString* rentType;

@property(nonatomic, copy) NSString* posterUri;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* rentAreas;

@property(nonatomic, copy) NSString* contacts;

@property(nonatomic, copy) NSString* contactPhone;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* enterTime;

@property(nonatomic, copy) NSNumber* status;

// item type EvhBuildingForRentAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

