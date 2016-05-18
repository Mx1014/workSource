//
// EvhVersionRequestCommand.m
//
#import "EvhVersionRequestCommand.h"
#import "EvhVersionDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionRequestCommand
//

@implementation EvhVersionRequestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVersionRequestCommand* obj = [EvhVersionRequestCommand new];
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
    if(self.realm)
        [jsonObject setObject: self.realm forKey: @"realm"];
    if(self.currentVersion) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.currentVersion toJson: dic];
        
        [jsonObject setObject: dic forKey: @"currentVersion"];
    }
    if(self.locale)
        [jsonObject setObject: self.locale forKey: @"locale"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.realm = [jsonObject objectForKey: @"realm"];
        if(self.realm && [self.realm isEqual:[NSNull null]])
            self.realm = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"currentVersion"];

        self.currentVersion = [EvhVersionDTO new];
        self.currentVersion = [self.currentVersion fromJson: itemJson];
        self.locale = [jsonObject objectForKey: @"locale"];
        if(self.locale && [self.locale isEqual:[NSNull null]])
            self.locale = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
