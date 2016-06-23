//
// EvhActivityTokenDTO.m
//
#import "EvhActivityTokenDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityTokenDTO
//

@implementation EvhActivityTokenDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityTokenDTO* obj = [EvhActivityTokenDTO new];
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
    if(self.postId)
        [jsonObject setObject: self.postId forKey: @"postId"];
    if(self.forumId)
        [jsonObject setObject: self.forumId forKey: @"forumId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.postId = [jsonObject objectForKey: @"postId"];
        if(self.postId && [self.postId isEqual:[NSNull null]])
            self.postId = nil;

        self.forumId = [jsonObject objectForKey: @"forumId"];
        if(self.forumId && [self.forumId isEqual:[NSNull null]])
            self.forumId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
