//
// EvhGetUserInfoByIdCommand.m
//
#import "EvhGetUserInfoByIdCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserInfoByIdCommand
//

@implementation EvhGetUserInfoByIdCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetUserInfoByIdCommand* obj = [EvhGetUserInfoByIdCommand new];
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
    if(self.zlAppKey)
        [jsonObject setObject: self.zlAppKey forKey: @"zlAppKey"];
    if(self.zlSignature)
        [jsonObject setObject: self.zlSignature forKey: @"zlSignature"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.randomNum)
        [jsonObject setObject: self.randomNum forKey: @"randomNum"];
    if(self.timeStamp)
        [jsonObject setObject: self.timeStamp forKey: @"timeStamp"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.zlAppKey = [jsonObject objectForKey: @"zlAppKey"];
        if(self.zlAppKey && [self.zlAppKey isEqual:[NSNull null]])
            self.zlAppKey = nil;

        self.zlSignature = [jsonObject objectForKey: @"zlSignature"];
        if(self.zlSignature && [self.zlSignature isEqual:[NSNull null]])
            self.zlSignature = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.randomNum = [jsonObject objectForKey: @"randomNum"];
        if(self.randomNum && [self.randomNum isEqual:[NSNull null]])
            self.randomNum = nil;

        self.timeStamp = [jsonObject objectForKey: @"timeStamp"];
        if(self.timeStamp && [self.timeStamp isEqual:[NSNull null]])
            self.timeStamp = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
