//
// EvhListPropTopicStatisticCommand.m
//
#import "EvhListPropTopicStatisticCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropTopicStatisticCommand
//

@implementation EvhListPropTopicStatisticCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPropTopicStatisticCommand* obj = [EvhListPropTopicStatisticCommand new];
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
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.startStrTime)
        [jsonObject setObject: self.startStrTime forKey: @"startStrTime"];
    if(self.endStrTime)
        [jsonObject setObject: self.endStrTime forKey: @"endStrTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        self.startStrTime = [jsonObject objectForKey: @"startStrTime"];
        if(self.startStrTime && [self.startStrTime isEqual:[NSNull null]])
            self.startStrTime = nil;

        self.endStrTime = [jsonObject objectForKey: @"endStrTime"];
        if(self.endStrTime && [self.endStrTime isEqual:[NSNull null]])
            self.endStrTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
