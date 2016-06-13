//
// EvhListComunitiesByKeywordCommand.m
//
#import "EvhListComunitiesByKeywordCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListComunitiesByKeywordCommand
//

@implementation EvhListComunitiesByKeywordCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListComunitiesByKeywordCommand* obj = [EvhListComunitiesByKeywordCommand new];
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
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
