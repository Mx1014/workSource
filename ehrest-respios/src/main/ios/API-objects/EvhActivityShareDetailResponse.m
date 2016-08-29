//
// EvhActivityShareDetailResponse.m
//
#import "EvhActivityShareDetailResponse.h"
#import "EvhActivityDTO.h"
#import "EvhAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityShareDetailResponse
//

@implementation EvhActivityShareDetailResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityShareDetailResponse* obj = [EvhActivityShareDetailResponse new];
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
    if(self.activity) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.activity toJson: dic];
        
        [jsonObject setObject: dic forKey: @"activity"];
    }
    if(self.content)
        [jsonObject setObject: self.content forKey: @"content"];
    if(self.childCount)
        [jsonObject setObject: self.childCount forKey: @"childCount"];
    if(self.viewCount)
        [jsonObject setObject: self.viewCount forKey: @"viewCount"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.creatorNickName)
        [jsonObject setObject: self.creatorNickName forKey: @"creatorNickName"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhAttachmentDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
    if(self.creatorAvatarUrl)
        [jsonObject setObject: self.creatorAvatarUrl forKey: @"creatorAvatarUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"activity"];

        self.activity = [EvhActivityDTO new];
        self.activity = [self.activity fromJson: itemJson];
        self.content = [jsonObject objectForKey: @"content"];
        if(self.content && [self.content isEqual:[NSNull null]])
            self.content = nil;

        self.childCount = [jsonObject objectForKey: @"childCount"];
        if(self.childCount && [self.childCount isEqual:[NSNull null]])
            self.childCount = nil;

        self.viewCount = [jsonObject objectForKey: @"viewCount"];
        if(self.viewCount && [self.viewCount isEqual:[NSNull null]])
            self.viewCount = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.creatorNickName = [jsonObject objectForKey: @"creatorNickName"];
        if(self.creatorNickName && [self.creatorNickName isEqual:[NSNull null]])
            self.creatorNickName = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhAttachmentDTO* item = [EvhAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        self.creatorAvatarUrl = [jsonObject objectForKey: @"creatorAvatarUrl"];
        if(self.creatorAvatarUrl && [self.creatorAvatarUrl isEqual:[NSNull null]])
            self.creatorAvatarUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
