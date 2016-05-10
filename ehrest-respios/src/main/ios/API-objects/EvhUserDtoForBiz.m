//
// EvhUserDtoForBiz.m
//
#import "EvhUserDtoForBiz.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserDtoForBiz
//

@implementation EvhUserDtoForBiz

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserDtoForBiz* obj = [EvhUserDtoForBiz new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
