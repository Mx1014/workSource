//
// EvhOpPromotionMessageDTO.m
//
#import "EvhOpPromotionMessageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionMessageDTO
//

@implementation EvhOpPromotionMessageDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhOpPromotionMessageDTO* obj = [EvhOpPromotionMessageDTO new];
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
    if(self.messageSeq)
        [jsonObject setObject: self.messageSeq forKey: @"messageSeq"];
    if(self.metaAppId)
        [jsonObject setObject: self.metaAppId forKey: @"metaAppId"];
    if(self.resultData)
        [jsonObject setObject: self.resultData forKey: @"resultData"];
    if(self.messageText)
        [jsonObject setObject: self.messageText forKey: @"messageText"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.messageMeta)
        [jsonObject setObject: self.messageMeta forKey: @"messageMeta"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.senderUid)
        [jsonObject setObject: self.senderUid forKey: @"senderUid"];
    if(self.targetUid)
        [jsonObject setObject: self.targetUid forKey: @"targetUid"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.messageSeq = [jsonObject objectForKey: @"messageSeq"];
        if(self.messageSeq && [self.messageSeq isEqual:[NSNull null]])
            self.messageSeq = nil;

        self.metaAppId = [jsonObject objectForKey: @"metaAppId"];
        if(self.metaAppId && [self.metaAppId isEqual:[NSNull null]])
            self.metaAppId = nil;

        self.resultData = [jsonObject objectForKey: @"resultData"];
        if(self.resultData && [self.resultData isEqual:[NSNull null]])
            self.resultData = nil;

        self.messageText = [jsonObject objectForKey: @"messageText"];
        if(self.messageText && [self.messageText isEqual:[NSNull null]])
            self.messageText = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.messageMeta = [jsonObject objectForKey: @"messageMeta"];
        if(self.messageMeta && [self.messageMeta isEqual:[NSNull null]])
            self.messageMeta = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.senderUid = [jsonObject objectForKey: @"senderUid"];
        if(self.senderUid && [self.senderUid isEqual:[NSNull null]])
            self.senderUid = nil;

        self.targetUid = [jsonObject objectForKey: @"targetUid"];
        if(self.targetUid && [self.targetUid isEqual:[NSNull null]])
            self.targetUid = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
