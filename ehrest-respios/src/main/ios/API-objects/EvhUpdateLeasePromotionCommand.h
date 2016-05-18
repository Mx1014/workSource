//
// EvhUpdateLeasePromotionCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBuildingForRentAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateLeasePromotionCommand
//
@interface EvhUpdateLeasePromotionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

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

// item type EvhBuildingForRentAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

