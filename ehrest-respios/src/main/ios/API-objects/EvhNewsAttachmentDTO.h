//
// EvhNewsAttachmentDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsAttachmentDTO
//
@interface EvhNewsAttachmentDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* contentUri;

@property(nonatomic, copy) NSString* contentUrl;

@property(nonatomic, copy) NSNumber* size;

@property(nonatomic, copy) NSString* metadata;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

