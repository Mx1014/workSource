//
// EvhAddNewsCommentResponse.m
//
#import "EvhAddNewsCommentResponse.h"
#import "EvhNewsAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddNewsCommentResponse
//

@implementation EvhAddNewsCommentResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAddNewsCommentResponse* obj = [EvhAddNewsCommentResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _attachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.Id)
        [jsonObject setObject: self.Id forKey: @"Id"];
    if(self.theNewsToken)
        [jsonObject setObject: self.theNewsToken forKey: @"newsToken"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.creatorNickName)
        [jsonObject setObject: self.creatorNickName forKey: @"creatorNickName"];
    if(self.creatorAvatar)
        [jsonObject setObject: self.creatorAvatar forKey: @"creatorAvatar"];
    if(self.creatorAvatarUrl)
        [jsonObject setObject: self.creatorAvatarUrl forKey: @"creatorAvatarUrl"];
    if(self.contentType)
        [jsonObject setObject: self.contentType forKey: @"contentType"];
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhNewsAttachmentDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.deleteFlag)
        [jsonObject setObject: self.deleteFlag forKey: @"deleteFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.Id = [jsonObject objectForKey: @"Id"];
        if(self.Id && [self.Id isEqual:[NSNull null]])
            self.Id = nil;

        self.theNewsToken = [jsonObject objectForKey: @"newsToken"];
        if(self.theNewsToken && [self.theNewsToken isEqual:[NSNull null]])
            self.theNewsToken = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.creatorNickName = [jsonObject objectForKey: @"creatorNickName"];
        if(self.creatorNickName && [self.creatorNickName isEqual:[NSNull null]])
            self.creatorNickName = nil;

        self.creatorAvatar = [jsonObject objectForKey: @"creatorAvatar"];
        if(self.creatorAvatar && [self.creatorAvatar isEqual:[NSNull null]])
            self.creatorAvatar = nil;

        self.creatorAvatarUrl = [jsonObject objectForKey: @"creatorAvatarUrl"];
        if(self.creatorAvatarUrl && [self.creatorAvatarUrl isEqual:[NSNull null]])
            self.creatorAvatarUrl = nil;

        self.contentType = [jsonObject objectForKey: @"contentType"];
        if(self.contentType && [self.contentType isEqual:[NSNull null]])
            self.contentType = nil;

        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhNewsAttachmentDTO* item = [EvhNewsAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        self.deleteFlag = [jsonObject objectForKey: @"deleteFlag"];
        if(self.deleteFlag && [self.deleteFlag isEqual:[NSNull null]])
            self.deleteFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
