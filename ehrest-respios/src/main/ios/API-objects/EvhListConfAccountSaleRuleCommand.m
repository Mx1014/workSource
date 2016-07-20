//
// EvhListConfAccountSaleRuleCommand.m
//
#import "EvhListConfAccountSaleRuleCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListConfAccountSaleRuleCommand
//

@implementation EvhListConfAccountSaleRuleCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListConfAccountSaleRuleCommand* obj = [EvhListConfAccountSaleRuleCommand new];
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
    if(self.confType)
        [jsonObject setObject: self.confType forKey: @"confType"];
    if(self.isOnline)
        [jsonObject setObject: self.isOnline forKey: @"isOnline"];
    if(self.pageOffset)
        [jsonObject setObject: self.pageOffset forKey: @"pageOffset"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.confType = [jsonObject objectForKey: @"confType"];
        if(self.confType && [self.confType isEqual:[NSNull null]])
            self.confType = nil;

        self.isOnline = [jsonObject objectForKey: @"isOnline"];
        if(self.isOnline && [self.isOnline isEqual:[NSNull null]])
            self.isOnline = nil;

        self.pageOffset = [jsonObject objectForKey: @"pageOffset"];
        if(self.pageOffset && [self.pageOffset isEqual:[NSNull null]])
            self.pageOffset = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
