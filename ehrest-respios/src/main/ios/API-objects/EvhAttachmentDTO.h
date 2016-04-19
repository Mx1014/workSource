//
// EvhAttachmentDTO.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAttachmentDTO
//
@interface EvhAttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* postId;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* contentUri;

@property(nonatomic, copy) NSString* contentUrl;

@property(nonatomic, copy) NSNumber* size;

@property(nonatomic, copy) NSString* metadata;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

