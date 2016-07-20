//
// EvhAppCreateCommand.m
//
#import "EvhAppCreateCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppCreateCommand
//

@implementation EvhAppCreateCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAppCreateCommand* obj = [EvhAppCreateCommand new];
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
    if(self.appKey)
        [jsonObject setObject: self.appKey forKey: @"appKey"];
    if(self.creatorUid)
        [jsonObject setObject: self.creatorUid forKey: @"creatorUid"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.secretKey)
        [jsonObject setObject: self.secretKey forKey: @"secretKey"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.appKey = [jsonObject objectForKey: @"appKey"];
        if(self.appKey && [self.appKey isEqual:[NSNull null]])
            self.appKey = nil;

        self.creatorUid = [jsonObject objectForKey: @"creatorUid"];
        if(self.creatorUid && [self.creatorUid isEqual:[NSNull null]])
            self.creatorUid = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.secretKey = [jsonObject objectForKey: @"secretKey"];
        if(self.secretKey && [self.secretKey isEqual:[NSNull null]])
            self.secretKey = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
