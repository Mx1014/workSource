//
// EvhMoreActionData.m
//
#import "EvhMoreActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMoreActionData
//

@implementation EvhMoreActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhMoreActionData* obj = [EvhMoreActionData new];
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
    if(self.itemLocation)
        [jsonObject setObject: self.itemLocation forKey: @"itemLocation"];
    if(self.itemGroup)
        [jsonObject setObject: self.itemGroup forKey: @"itemGroup"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.itemLocation = [jsonObject objectForKey: @"itemLocation"];
        if(self.itemLocation && [self.itemLocation isEqual:[NSNull null]])
            self.itemLocation = nil;

        self.itemGroup = [jsonObject objectForKey: @"itemGroup"];
        if(self.itemGroup && [self.itemGroup isEqual:[NSNull null]])
            self.itemGroup = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
