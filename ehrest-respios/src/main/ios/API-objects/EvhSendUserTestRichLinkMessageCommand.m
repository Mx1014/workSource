//
// EvhSendUserTestRichLinkMessageCommand.m
//
#import "EvhSendUserTestRichLinkMessageCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendUserTestRichLinkMessageCommand
//

@implementation EvhSendUserTestRichLinkMessageCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSendUserTestRichLinkMessageCommand* obj = [EvhSendUserTestRichLinkMessageCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.title)
        [jsonObject setObject: self.title forKey: @"title"];
    if(self.coverUri)
        [jsonObject setObject: self.coverUri forKey: @"coverUri"];
    if(self.coverUrl)
        [jsonObject setObject: self.coverUrl forKey: @"coverUrl"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.actionUrl)
        [jsonObject setObject: self.actionUrl forKey: @"actionUrl"];
    if(self.targetNamespaceId)
        [jsonObject setObject: self.targetNamespaceId forKey: @"targetNamespaceId"];
    if(self.targetPhone)
        [jsonObject setObject: self.targetPhone forKey: @"targetPhone"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.title = [jsonObject objectForKey: @"title"];
        if(self.title && [self.title isEqual:[NSNull null]])
            self.title = nil;

        self.coverUri = [jsonObject objectForKey: @"coverUri"];
        if(self.coverUri && [self.coverUri isEqual:[NSNull null]])
            self.coverUri = nil;

        self.coverUrl = [jsonObject objectForKey: @"coverUrl"];
        if(self.coverUrl && [self.coverUrl isEqual:[NSNull null]])
            self.coverUrl = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.actionUrl = [jsonObject objectForKey: @"actionUrl"];
        if(self.actionUrl && [self.actionUrl isEqual:[NSNull null]])
            self.actionUrl = nil;

        self.targetNamespaceId = [jsonObject objectForKey: @"targetNamespaceId"];
        if(self.targetNamespaceId && [self.targetNamespaceId isEqual:[NSNull null]])
            self.targetNamespaceId = nil;

        self.targetPhone = [jsonObject objectForKey: @"targetPhone"];
        if(self.targetPhone && [self.targetPhone isEqual:[NSNull null]])
            self.targetPhone = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
