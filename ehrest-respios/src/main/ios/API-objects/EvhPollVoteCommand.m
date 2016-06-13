//
// EvhPollVoteCommand.m
//
#import "EvhPollVoteCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollVoteCommand
//

@implementation EvhPollVoteCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPollVoteCommand* obj = [EvhPollVoteCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _itemIds = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.pollId)
        [jsonObject setObject: self.pollId forKey: @"pollId"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.itemIds) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.itemIds) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"itemIds"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pollId = [jsonObject objectForKey: @"pollId"];
        if(self.pollId && [self.pollId isEqual:[NSNull null]])
            self.pollId = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"itemIds"];
            for(id itemJson in jsonArray) {
                [self.itemIds addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
