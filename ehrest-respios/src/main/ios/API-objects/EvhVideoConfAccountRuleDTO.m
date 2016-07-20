//
// EvhVideoConfAccountRuleDTO.m
//
#import "EvhVideoConfAccountRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountRuleDTO
//

@implementation EvhVideoConfAccountRuleDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVideoConfAccountRuleDTO* obj = [EvhVideoConfAccountRuleDTO new];
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
    if(self.multipleAccountThreshold)
        [jsonObject setObject: self.multipleAccountThreshold forKey: @"multipleAccountThreshold"];
    if(self.confCapacity)
        [jsonObject setObject: self.confCapacity forKey: @"confCapacity"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
    if(self.minPeriod)
        [jsonObject setObject: self.minPeriod forKey: @"minPeriod"];
    if(self.singleAccountPrice)
        [jsonObject setObject: self.singleAccountPrice forKey: @"singleAccountPrice"];
    if(self.multipleAccountPrice)
        [jsonObject setObject: self.multipleAccountPrice forKey: @"multipleAccountPrice"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.multipleAccountThreshold = [jsonObject objectForKey: @"multipleAccountThreshold"];
        if(self.multipleAccountThreshold && [self.multipleAccountThreshold isEqual:[NSNull null]])
            self.multipleAccountThreshold = nil;

        self.confCapacity = [jsonObject objectForKey: @"confCapacity"];
        if(self.confCapacity && [self.confCapacity isEqual:[NSNull null]])
            self.confCapacity = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        self.minPeriod = [jsonObject objectForKey: @"minPeriod"];
        if(self.minPeriod && [self.minPeriod isEqual:[NSNull null]])
            self.minPeriod = nil;

        self.singleAccountPrice = [jsonObject objectForKey: @"singleAccountPrice"];
        if(self.singleAccountPrice && [self.singleAccountPrice isEqual:[NSNull null]])
            self.singleAccountPrice = nil;

        self.multipleAccountPrice = [jsonObject objectForKey: @"multipleAccountPrice"];
        if(self.multipleAccountPrice && [self.multipleAccountPrice isEqual:[NSNull null]])
            self.multipleAccountPrice = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
