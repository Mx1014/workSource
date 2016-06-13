//
// EvhFetchCardCommand.m
//
#import "EvhFetchCardCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchCardCommand
//

@implementation EvhFetchCardCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFetchCardCommand* obj = [EvhFetchCardCommand new];
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
    if(self.applierPhone)
        [jsonObject setObject: self.applierPhone forKey: @"applierPhone"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.applierPhone = [jsonObject objectForKey: @"applierPhone"];
        if(self.applierPhone && [self.applierPhone isEqual:[NSNull null]])
            self.applierPhone = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
