//
// EvhQRCodeDTO.m
//
#import "EvhQRCodeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQRCodeDTO
//

@implementation EvhQRCodeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQRCodeDTO* obj = [EvhQRCodeDTO new];
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
    if(self.qrid)
        [jsonObject setObject: self.qrid forKey: @"qrid"];
    if(self.logoUri)
        [jsonObject setObject: self.logoUri forKey: @"logoUri"];
    if(self.logoUrl)
        [jsonObject setObject: self.logoUrl forKey: @"logoUrl"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.expireTime)
        [jsonObject setObject: self.expireTime forKey: @"expireTime"];
    if(self.actionType)
        [jsonObject setObject: self.actionType forKey: @"actionType"];
    if(self.actionData)
        [jsonObject setObject: self.actionData forKey: @"actionData"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.qrid = [jsonObject objectForKey: @"qrid"];
        if(self.qrid && [self.qrid isEqual:[NSNull null]])
            self.qrid = nil;

        self.logoUri = [jsonObject objectForKey: @"logoUri"];
        if(self.logoUri && [self.logoUri isEqual:[NSNull null]])
            self.logoUri = nil;

        self.logoUrl = [jsonObject objectForKey: @"logoUrl"];
        if(self.logoUrl && [self.logoUrl isEqual:[NSNull null]])
            self.logoUrl = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.expireTime = [jsonObject objectForKey: @"expireTime"];
        if(self.expireTime && [self.expireTime isEqual:[NSNull null]])
            self.expireTime = nil;

        self.actionType = [jsonObject objectForKey: @"actionType"];
        if(self.actionType && [self.actionType isEqual:[NSNull null]])
            self.actionType = nil;

        self.actionData = [jsonObject objectForKey: @"actionData"];
        if(self.actionData && [self.actionData isEqual:[NSNull null]])
            self.actionData = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
