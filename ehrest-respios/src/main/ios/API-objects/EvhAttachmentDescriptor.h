//
// EvhAttachmentDescriptor.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAttachmentDescriptor
//
@interface EvhAttachmentDescriptor
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contentType;

@property(nonatomic, copy) NSString* contentUri;

@property(nonatomic, copy) NSString* contentUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

