//
// EvhRentalBillAttachmentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalBillAttachmentDTO
//
@interface EvhRentalBillAttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSNumber* attachmentType;

@property(nonatomic, copy) NSString* content;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

