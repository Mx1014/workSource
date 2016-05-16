//
// EvhBorderDTO.m
//
#import "EvhBorderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBorderDTO
//

@implementation EvhBorderDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBorderDTO* obj = [EvhBorderDTO new];
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
    if(self.privateAddress)
        [jsonObject setObject: self.privateAddress forKey: @"privateAddress"];
    if(self.privatePort)
        [jsonObject setObject: self.privatePort forKey: @"privatePort"];
    if(self.publicAddress)
        [jsonObject setObject: self.publicAddress forKey: @"publicAddress"];
    if(self.publicPort)
        [jsonObject setObject: self.publicPort forKey: @"publicPort"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.configTag)
        [jsonObject setObject: self.configTag forKey: @"configTag"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

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

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.configTag = [jsonObject objectForKey: @"configTag"];
        if(self.configTag && [self.configTag isEqual:[NSNull null]])
            self.configTag = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
