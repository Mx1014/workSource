//
// EvhPollPostCommand.m
//
#import "EvhPollPostCommand.h"
#import "EvhPollItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollPostCommand
//

@implementation EvhPollPostCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPollPostCommand* obj = [EvhPollPostCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _itemList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.stopTime)
        [jsonObject setObject: self.stopTime forKey: @"stopTime"];
    if(self.multiChoiceFlag)
        [jsonObject setObject: self.multiChoiceFlag forKey: @"multiChoiceFlag"];
    if(self.anonymousFlag)
        [jsonObject setObject: self.anonymousFlag forKey: @"anonymousFlag"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.itemList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPollItemDTO* item in self.itemList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"itemList"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.stopTime = [jsonObject objectForKey: @"stopTime"];
        if(self.stopTime && [self.stopTime isEqual:[NSNull null]])
            self.stopTime = nil;

        self.multiChoiceFlag = [jsonObject objectForKey: @"multiChoiceFlag"];
        if(self.multiChoiceFlag && [self.multiChoiceFlag isEqual:[NSNull null]])
            self.multiChoiceFlag = nil;

        self.anonymousFlag = [jsonObject objectForKey: @"anonymousFlag"];
        if(self.anonymousFlag && [self.anonymousFlag isEqual:[NSNull null]])
            self.anonymousFlag = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"itemList"];
            for(id itemJson in jsonArray) {
                EvhPollItemDTO* item = [EvhPollItemDTO new];
                
                [item fromJson: itemJson];
                [self.itemList addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
