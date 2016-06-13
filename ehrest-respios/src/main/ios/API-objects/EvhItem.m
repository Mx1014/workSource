//
// EvhItem.m
//
#import "EvhItem.h"

///////////////////////////////////////////////////////////////////////////////
// EvhItem
//

@implementation EvhItem

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhItem* obj = [EvhItem new];
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
    if(self.orderIndex)
        [jsonObject setObject: self.orderIndex forKey: @"orderIndex"];
    if(self.applyPolicy)
        [jsonObject setObject: self.applyPolicy forKey: @"applyPolicy"];
    if(self.displayFlag)
        [jsonObject setObject: self.displayFlag forKey: @"displayFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.orderIndex = [jsonObject objectForKey: @"orderIndex"];
        if(self.orderIndex && [self.orderIndex isEqual:[NSNull null]])
            self.orderIndex = nil;

        self.applyPolicy = [jsonObject objectForKey: @"applyPolicy"];
        if(self.applyPolicy && [self.applyPolicy isEqual:[NSNull null]])
            self.applyPolicy = nil;

        self.displayFlag = [jsonObject objectForKey: @"displayFlag"];
        if(self.displayFlag && [self.displayFlag isEqual:[NSNull null]])
            self.displayFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
