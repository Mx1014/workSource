//
// EvhQuestionMetaObject.m
//
#import "EvhQuestionMetaObject.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQuestionMetaObject
//

@implementation EvhQuestionMetaObject

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQuestionMetaObject* obj = [EvhQuestionMetaObject new];
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
    if(self.resourceType)
        [jsonObject setObject: self.resourceType forKey: @"resourceType"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.requestorUid)
        [jsonObject setObject: self.requestorUid forKey: @"requestorUid"];
    if(self.requestorNickName)
        [jsonObject setObject: self.requestorNickName forKey: @"requestorNickName"];
    if(self.requestorAvatar)
        [jsonObject setObject: self.requestorAvatar forKey: @"requestorAvatar"];
    if(self.requestorAvatarUrl)
        [jsonObject setObject: self.requestorAvatarUrl forKey: @"requestorAvatarUrl"];
    if(self.requestTime)
        [jsonObject setObject: self.requestTime forKey: @"requestTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.resourceType = [jsonObject objectForKey: @"resourceType"];
        if(self.resourceType && [self.resourceType isEqual:[NSNull null]])
            self.resourceType = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.requestorUid = [jsonObject objectForKey: @"requestorUid"];
        if(self.requestorUid && [self.requestorUid isEqual:[NSNull null]])
            self.requestorUid = nil;

        self.requestorNickName = [jsonObject objectForKey: @"requestorNickName"];
        if(self.requestorNickName && [self.requestorNickName isEqual:[NSNull null]])
            self.requestorNickName = nil;

        self.requestorAvatar = [jsonObject objectForKey: @"requestorAvatar"];
        if(self.requestorAvatar && [self.requestorAvatar isEqual:[NSNull null]])
            self.requestorAvatar = nil;

        self.requestorAvatarUrl = [jsonObject objectForKey: @"requestorAvatarUrl"];
        if(self.requestorAvatarUrl && [self.requestorAvatarUrl isEqual:[NSNull null]])
            self.requestorAvatarUrl = nil;

        self.requestTime = [jsonObject objectForKey: @"requestTime"];
        if(self.requestTime && [self.requestTime isEqual:[NSNull null]])
            self.requestTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
