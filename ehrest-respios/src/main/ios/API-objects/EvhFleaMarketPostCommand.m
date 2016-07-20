//
// EvhFleaMarketPostCommand.m
//
#import "EvhFleaMarketPostCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFleaMarketPostCommand
//

@implementation EvhFleaMarketPostCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFleaMarketPostCommand* obj = [EvhFleaMarketPostCommand new];
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
    [super toJson: jsonObject];
    if(self.barterFlag)
        [jsonObject setObject: self.barterFlag forKey: @"barterFlag"];
    if(self.price)
        [jsonObject setObject: self.price forKey: @"price"];
    if(self.closeFlag)
        [jsonObject setObject: self.closeFlag forKey: @"closeFlag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.barterFlag = [jsonObject objectForKey: @"barterFlag"];
        if(self.barterFlag && [self.barterFlag isEqual:[NSNull null]])
            self.barterFlag = nil;

        self.price = [jsonObject objectForKey: @"price"];
        if(self.price && [self.price isEqual:[NSNull null]])
            self.price = nil;

        self.closeFlag = [jsonObject objectForKey: @"closeFlag"];
        if(self.closeFlag && [self.closeFlag isEqual:[NSNull null]])
            self.closeFlag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
