//
// EvhAttachmentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAttachmentDTO
//
@interface EvhAttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* attachmentType;

@property(nonatomic, copy) NSNumber* mustOptions;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

