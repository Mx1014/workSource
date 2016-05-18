//
// EvhLogonCommandResponse.m
//
#import "EvhLogonCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLogonCommandResponse
//

@implementation EvhLogonCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLogonCommandResponse* obj = [EvhLogonCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _accessPoints = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.loginToken)
        [jsonObject setObject: self.loginToken forKey: @"loginToken"];
    if(self.contentServer)
        [jsonObject setObject: self.contentServer forKey: @"contentServer"];
    if(self.accessPoints) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSString* item in self.accessPoints) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"accessPoints"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.loginToken = [jsonObject objectForKey: @"loginToken"];
        if(self.loginToken && [self.loginToken isEqual:[NSNull null]])
            self.loginToken = nil;

        self.contentServer = [jsonObject objectForKey: @"contentServer"];
        if(self.contentServer && [self.contentServer isEqual:[NSNull null]])
            self.contentServer = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"accessPoints"];
            for(id itemJson in jsonArray) {
                [self.accessPoints addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
