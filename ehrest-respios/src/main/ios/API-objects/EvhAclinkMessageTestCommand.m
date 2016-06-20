//
// EvhAclinkMessageTestCommand.m
//
#import "EvhAclinkMessageTestCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkMessageTestCommand
//

@implementation EvhAclinkMessageTestCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkMessageTestCommand* obj = [EvhAclinkMessageTestCommand new];
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
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.doorType)
        [jsonObject setObject: self.doorType forKey: @"doorType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.doorType = [jsonObject objectForKey: @"doorType"];
        if(self.doorType && [self.doorType isEqual:[NSNull null]])
            self.doorType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
