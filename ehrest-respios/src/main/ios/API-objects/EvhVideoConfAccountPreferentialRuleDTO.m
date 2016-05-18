//
// EvhVideoConfAccountPreferentialRuleDTO.m
//
#import "EvhVideoConfAccountPreferentialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountPreferentialRuleDTO
//

@implementation EvhVideoConfAccountPreferentialRuleDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVideoConfAccountPreferentialRuleDTO* obj = [EvhVideoConfAccountPreferentialRuleDTO new];
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
    if(self.limit)
        [jsonObject setObject: self.limit forKey: @"limit"];
    if(self.subtract)
        [jsonObject setObject: self.subtract forKey: @"subtract"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.limit = [jsonObject objectForKey: @"limit"];
        if(self.limit && [self.limit isEqual:[NSNull null]])
            self.limit = nil;

        self.subtract = [jsonObject objectForKey: @"subtract"];
        if(self.subtract && [self.subtract isEqual:[NSNull null]])
            self.subtract = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
