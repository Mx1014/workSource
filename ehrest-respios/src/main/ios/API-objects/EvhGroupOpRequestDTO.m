//
// EvhGroupOpRequestDTO.m
//
#import "EvhGroupOpRequestDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGroupOpRequestDTO
//

@implementation EvhGroupOpRequestDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGroupOpRequestDTO* obj = [EvhGroupOpRequestDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.groupId)
        [jsonObject setObject: self.groupId forKey: @"groupId"];
    if(self.groupName)
        [jsonObject setObject: self.groupName forKey: @"groupName"];
    if(self.requestorUid)
        [jsonObject setObject: self.requestorUid forKey: @"requestorUid"];
    if(self.requestorName)
        [jsonObject setObject: self.requestorName forKey: @"requestorName"];
    if(self.requestorAvatar)
        [jsonObject setObject: self.requestorAvatar forKey: @"requestorAvatar"];
    if(self.requestorAvatarUrl)
        [jsonObject setObject: self.requestorAvatarUrl forKey: @"requestorAvatarUrl"];
    if(self.requestorComment)
        [jsonObject setObject: self.requestorComment forKey: @"requestorComment"];
    if(self.operationType)
        [jsonObject setObject: self.operationType forKey: @"operationType"];
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.operatorUid)
        [jsonObject setObject: self.operatorUid forKey: @"operatorUid"];
    if(self.processMessage)
        [jsonObject setObject: self.processMessage forKey: @"processMessage"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
    if(self.processTime)
        [jsonObject setObject: self.processTime forKey: @"processTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.groupId = [jsonObject objectForKey: @"groupId"];
        if(self.groupId && [self.groupId isEqual:[NSNull null]])
            self.groupId = nil;

        self.groupName = [jsonObject objectForKey: @"groupName"];
        if(self.groupName && [self.groupName isEqual:[NSNull null]])
            self.groupName = nil;

        self.requestorUid = [jsonObject objectForKey: @"requestorUid"];
        if(self.requestorUid && [self.requestorUid isEqual:[NSNull null]])
            self.requestorUid = nil;

        self.requestorName = [jsonObject objectForKey: @"requestorName"];
        if(self.requestorName && [self.requestorName isEqual:[NSNull null]])
            self.requestorName = nil;

        self.requestorAvatar = [jsonObject objectForKey: @"requestorAvatar"];
        if(self.requestorAvatar && [self.requestorAvatar isEqual:[NSNull null]])
            self.requestorAvatar = nil;

        self.requestorAvatarUrl = [jsonObject objectForKey: @"requestorAvatarUrl"];
        if(self.requestorAvatarUrl && [self.requestorAvatarUrl isEqual:[NSNull null]])
            self.requestorAvatarUrl = nil;

        self.requestorComment = [jsonObject objectForKey: @"requestorComment"];
        if(self.requestorComment && [self.requestorComment isEqual:[NSNull null]])
            self.requestorComment = nil;

        self.operationType = [jsonObject objectForKey: @"operationType"];
        if(self.operationType && [self.operationType isEqual:[NSNull null]])
            self.operationType = nil;

        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.operatorUid = [jsonObject objectForKey: @"operatorUid"];
        if(self.operatorUid && [self.operatorUid isEqual:[NSNull null]])
            self.operatorUid = nil;

        self.processMessage = [jsonObject objectForKey: @"processMessage"];
        if(self.processMessage && [self.processMessage isEqual:[NSNull null]])
            self.processMessage = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        self.processTime = [jsonObject objectForKey: @"processTime"];
        if(self.processTime && [self.processTime isEqual:[NSNull null]])
            self.processTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
