//
// EvhSendUserTestRichLinkMessageCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendUserTestRichLinkMessageCommand
//
@interface EvhSendUserTestRichLinkMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSString* coverUri;

@property(nonatomic, copy) NSString* coverUrl;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSString* actionUrl;

@property(nonatomic, copy) NSNumber* targetNamespaceId;

@property(nonatomic, copy) NSString* targetPhone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

