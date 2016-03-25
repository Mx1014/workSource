//
// EvhVideoConfAccountRuleDTO.m
// generated at 2016-03-25 15:57:21 
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
    if(self.accountType)
        [jsonObject setObject: self.accountType forKey: @"accountType"];
    if(self.confCapacity)
        [jsonObject setObject: self.confCapacity forKey: @"confCapacity"];
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
    if(self.minimumMonths)
        [jsonObject setObject: self.minimumMonths forKey: @"minimumMonths"];
    if(self.packagePrice)
        [jsonObject setObject: self.packagePrice forKey: @"packagePrice"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.accountType = [jsonObject objectForKey: @"accountType"];
        if(self.accountType && [self.accountType isEqual:[NSNull null]])
            self.accountType = nil;

        self.confCapacity = [jsonObject objectForKey: @"confCapacity"];
        if(self.confCapacity && [self.confCapacity isEqual:[NSNull null]])
            self.confCapacity = nil;

        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        self.minimumMonths = [jsonObject objectForKey: @"minimumMonths"];
        if(self.minimumMonths && [self.minimumMonths isEqual:[NSNull null]])
            self.minimumMonths = nil;

        self.packagePrice = [jsonObject objectForKey: @"packagePrice"];
        if(self.packagePrice && [self.packagePrice isEqual:[NSNull null]])
            self.packagePrice = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
