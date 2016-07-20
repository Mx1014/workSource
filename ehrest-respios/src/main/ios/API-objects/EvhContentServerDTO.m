//
// EvhContentServerDTO.m
//
#import "EvhContentServerDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhContentServerDTO
//

@implementation EvhContentServerDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhContentServerDTO* obj = [EvhContentServerDTO new];
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
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.privateAddress)
        [jsonObject setObject: self.privateAddress forKey: @"privateAddress"];
    if(self.privatePort)
        [jsonObject setObject: self.privatePort forKey: @"privatePort"];
    if(self.publicAddress)
        [jsonObject setObject: self.publicAddress forKey: @"publicAddress"];
    if(self.publicPort)
        [jsonObject setObject: self.publicPort forKey: @"publicPort"];
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

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.privateAddress = [jsonObject objectForKey: @"privateAddress"];
        if(self.privateAddress && [self.privateAddress isEqual:[NSNull null]])
            self.privateAddress = nil;

        self.privatePort = [jsonObject objectForKey: @"privatePort"];
        if(self.privatePort && [self.privatePort isEqual:[NSNull null]])
            self.privatePort = nil;

        self.publicAddress = [jsonObject objectForKey: @"publicAddress"];
        if(self.publicAddress && [self.publicAddress isEqual:[NSNull null]])
            self.publicAddress = nil;

        self.publicPort = [jsonObject objectForKey: @"publicPort"];
        if(self.publicPort && [self.publicPort isEqual:[NSNull null]])
            self.publicPort = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
