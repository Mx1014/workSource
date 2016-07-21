//
// EvhRentalv2AttachmentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2AttachmentDTO
//
@interface EvhRentalv2AttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* attachmentType;

@property(nonatomic, copy) NSString* content;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

