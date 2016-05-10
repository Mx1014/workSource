//
// EvhListVideoConfAccountOrderCommand.m
//
#import "EvhListVideoConfAccountOrderCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountOrderCommand
//

@implementation EvhListVideoConfAccountOrderCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListVideoConfAccountOrderCommand* obj = [EvhListVideoConfAccountOrderCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
