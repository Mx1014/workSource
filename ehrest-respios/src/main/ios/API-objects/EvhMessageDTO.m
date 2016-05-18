//
// EvhMessageDTO.m
//
#import "EvhMessageDTO.h"
#import "EvhMessageChannel.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMessageDTO
//

@implementation EvhMessageDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhMessageDTO* obj = [EvhMessageDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _channels = [NSMutableArray new];
        _meta = [NSMutableDictionary new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.appId)
        [jsonObject setObject: self.appId forKey: @"appId"];
    if(self.senderUid)
        [jsonObject setObject: self.senderUid forKey: @"senderUid"];
    if(self.contextType)
        [jsonObject setObject: self.contextType forKey: @"contextType"];
    if(self.contextToken)
        [jsonObject setObject: self.contextToken forKey: @"contextToken"];
    if(self.channels) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhMessageChannel* item in self.channels) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"channels"];
    }
    if(self.meta) {
        NSMutableDictionary* jsonMap = [NSMutableDictionary new];
        for(NSString* key in self.meta) {
            [jsonMap setValue:[self.meta objectForKey: key] forKey: key];
        }
        [jsonObject setObject: jsonMap forKey: @"meta"];
    }        
    if(self.bodyType)
        [jsonObject setObject: self.bodyType forKey: @"bodyType"];
    if(self.body)
        [jsonObject setObject: self.body forKey: @"body"];
    if(self.senderTag)
        [jsonObject setObject: self.senderTag forKey: @"senderTag"];
    if(self.storeSequence)
        [jsonObject setObject: self.storeSequence forKey: @"storeSequence"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appId = [jsonObject objectForKey: @"appId"];
        if(self.appId && [self.appId isEqual:[NSNull null]])
            self.appId = nil;

        self.senderUid = [jsonObject objectForKey: @"senderUid"];
        if(self.senderUid && [self.senderUid isEqual:[NSNull null]])
            self.senderUid = nil;

        self.contextType = [jsonObject objectForKey: @"contextType"];
        if(self.contextType && [self.contextType isEqual:[NSNull null]])
            self.contextType = nil;

        self.contextToken = [jsonObject objectForKey: @"contextToken"];
        if(self.contextToken && [self.contextToken isEqual:[NSNull null]])
            self.contextToken = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"channels"];
            for(id itemJson in jsonArray) {
                EvhMessageChannel* item = [EvhMessageChannel new];
                
                [item fromJson: itemJson];
                [self.channels addObject: item];
            }
        }
        {
            NSDictionary* jsonMap = [jsonObject objectForKey: @"meta"];
            for(NSString* key in jsonMap) {
                [self.meta setObject: [jsonMap objectForKey: key] forKey: key];
            }
        }
        self.bodyType = [jsonObject objectForKey: @"bodyType"];
        if(self.bodyType && [self.bodyType isEqual:[NSNull null]])
            self.bodyType = nil;

        self.body = [jsonObject objectForKey: @"body"];
        if(self.body && [self.body isEqual:[NSNull null]])
            self.body = nil;

        self.senderTag = [jsonObject objectForKey: @"senderTag"];
        if(self.senderTag && [self.senderTag isEqual:[NSNull null]])
            self.senderTag = nil;

        self.storeSequence = [jsonObject objectForKey: @"storeSequence"];
        if(self.storeSequence && [self.storeSequence isEqual:[NSNull null]])
            self.storeSequence = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
