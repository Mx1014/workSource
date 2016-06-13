//
// EvhExchangeHallActionData.m
//
#import "EvhExchangeHallActionData.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExchangeHallActionData
//

@implementation EvhExchangeHallActionData

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhExchangeHallActionData* obj = [EvhExchangeHallActionData new];
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
    if(self.categoryId)
        [jsonObject setObject: self.categoryId forKey: @"categoryId"];
    if(self.tag)
        [jsonObject setObject: self.tag forKey: @"tag"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.categoryId = [jsonObject objectForKey: @"categoryId"];
        if(self.categoryId && [self.categoryId isEqual:[NSNull null]])
            self.categoryId = nil;

        self.tag = [jsonObject objectForKey: @"tag"];
        if(self.tag && [self.tag isEqual:[NSNull null]])
            self.tag = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
