//
// EvhGetSignatureCommandResponse.m
//
#import "EvhGetSignatureCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetSignatureCommandResponse
//

@implementation EvhGetSignatureCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetSignatureCommandResponse* obj = [EvhGetSignatureCommandResponse new];
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
    if(self.signature)
        [jsonObject setObject: self.signature forKey: @"signature"];
    if(self.appKey)
        [jsonObject setObject: self.appKey forKey: @"appKey"];
    if(self.timeStamp)
        [jsonObject setObject: self.timeStamp forKey: @"timeStamp"];
    if(self.randomNum)
        [jsonObject setObject: self.randomNum forKey: @"randomNum"];
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

        self.signature = [jsonObject objectForKey: @"signature"];
        if(self.signature && [self.signature isEqual:[NSNull null]])
            self.signature = nil;

        self.appKey = [jsonObject objectForKey: @"appKey"];
        if(self.appKey && [self.appKey isEqual:[NSNull null]])
            self.appKey = nil;

        self.timeStamp = [jsonObject objectForKey: @"timeStamp"];
        if(self.timeStamp && [self.timeStamp isEqual:[NSNull null]])
            self.timeStamp = nil;

        self.randomNum = [jsonObject objectForKey: @"randomNum"];
        if(self.randomNum && [self.randomNum isEqual:[NSNull null]])
            self.randomNum = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
