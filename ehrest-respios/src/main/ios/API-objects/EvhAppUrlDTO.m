//
// EvhAppUrlDTO.m
//
#import "EvhAppUrlDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppUrlDTO
//

@implementation EvhAppUrlDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAppUrlDTO* obj = [EvhAppUrlDTO new];
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
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.logoUrl)
        [jsonObject setObject: self.logoUrl forKey: @"logoUrl"];
    if(self.downloadUrl)
        [jsonObject setObject: self.downloadUrl forKey: @"downloadUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.logoUrl = [jsonObject objectForKey: @"logoUrl"];
        if(self.logoUrl && [self.logoUrl isEqual:[NSNull null]])
            self.logoUrl = nil;

        self.downloadUrl = [jsonObject objectForKey: @"downloadUrl"];
        if(self.downloadUrl && [self.downloadUrl isEqual:[NSNull null]])
            self.downloadUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
