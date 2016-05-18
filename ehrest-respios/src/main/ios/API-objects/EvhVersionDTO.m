//
// EvhVersionDTO.m
//
#import "EvhVersionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionDTO
//

@implementation EvhVersionDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVersionDTO* obj = [EvhVersionDTO new];
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
    if(self.major)
        [jsonObject setObject: self.major forKey: @"major"];
    if(self.minor)
        [jsonObject setObject: self.minor forKey: @"minor"];
    if(self.revision)
        [jsonObject setObject: self.revision forKey: @"revision"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.major = [jsonObject objectForKey: @"major"];
        if(self.major && [self.major isEqual:[NSNull null]])
            self.major = nil;

        self.minor = [jsonObject objectForKey: @"minor"];
        if(self.minor && [self.minor isEqual:[NSNull null]])
            self.minor = nil;

        self.revision = [jsonObject objectForKey: @"revision"];
        if(self.revision && [self.revision isEqual:[NSNull null]])
            self.revision = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
