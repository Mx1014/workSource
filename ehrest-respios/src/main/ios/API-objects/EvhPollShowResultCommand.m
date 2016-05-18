//
// EvhPollShowResultCommand.m
//
#import "EvhPollShowResultCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPollShowResultCommand
//

@implementation EvhPollShowResultCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPollShowResultCommand* obj = [EvhPollShowResultCommand new];
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
    if(self.pollId)
        [jsonObject setObject: self.pollId forKey: @"pollId"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
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

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
